package com.smartassistant.app

data class ExtractedCustomer(
    val name: String,
    val phone: String = "",
    val balance: Double = 0.0
)

data class ExtractedItem(
    val name: String,
    val price: Double = 0.0,
    val quantity: Double = 0.0
)

object PdfImportEngine {

    // الكلمات الدليلية للترويسات والعناوين التي سيتم تجاهلها تلقائياً
    private val IGNORED_HEADER_KEYWORDS = listOf(
        "صفحة", "page", "كشف", "تقرير", "بيان", "العنوان", "التاريخ", "الهاتف",
        "اسم العميل", "رقم الهاتف", "الرصيد", "اسم الصنف", "السعر", "الكمية",
        "الكود", "الرمز", "إجمالي", "المجموع", "الشركة", "مؤسسة", "ملاحظات",
        "رقم الحساب", "العميل", "الصنف", "الوحدة", "التفاصيل", "الرقم"
    )

    // استخراج واستنتاج بيانات العملاء
    fun extractCustomers(rawText: String): List<ExtractedCustomer> {
        val lines = rawText.lines()
        val resultList = mutableListOf<ExtractedCustomer>()

        for (line in lines) {
            val cleanedLine = line.trim()
            if (isNoiseOrHeader(cleanedLine)) continue

            // البحث عن أرقام الهواتف (مثل 77XXXXXXX أو 05XXXXXXX أو الأرقام الدولية)
            val phoneRegex = Regex("""(7\d{8}|0\d{9}|\+?\d{9,12})""")
            val phoneMatch = phoneRegex.find(cleanedLine)?.value ?: ""

            // استخراج الأرقام العادية (الأرصدة أو المبالغ)
            val numberMatches = Regex("""\d+(\.\d+)?""").findAll(cleanedLine).map { it.value }.toList()

            // تنظيف النص للحصول على اسم العميل فقط
            var nameOnly = cleanedLine
                .replace(phoneRegex, "")
                .replace(Regex("""\d+(\.\d+)?"""), "")
                .replace(Regex("""[|\t\-:_#*/\\]"""), " ")
                .trim()

            // تنظيف المساحات الزائدة
            nameOnly = nameOnly.replace(Regex("""\s+"""), " ")

            if (nameOnly.length >= 3) {
                val balanceVal = numberMatches
                    .lastOrNull { it != phoneMatch }
                    ?.toDoubleOrNull() ?: 0.0

                resultList.add(ExtractedCustomer(name = nameOnly, phone = phoneMatch, balance = balanceVal))
            }
        }
        return resultList
    }

    // استخراج واستنتاج بيانات الأصناف
    fun extractItems(rawText: String): List<ExtractedItem> {
        val lines = rawText.lines()
        val resultList = mutableListOf<ExtractedItem>()

        for (line in lines) {
            val cleanedLine = line.trim()
            if (isNoiseOrHeader(cleanedLine)) continue

            // استخراج كافة الأرقام المقاسة (الأسعار والأعداد)
            val numbers = Regex("""\d+(\.\d+)?""").findAll(cleanedLine)
                .mapNotNull { it.value.toDoubleOrNull() }
                .toList()

            // إزالة الأرقام والرموز لاستخراج اسم الصنف
            var nameOnly = cleanedLine
                .replace(Regex("""\d+(\.\d+)?"""), "")
                .replace(Regex("""[|\t\-:_#*/\\]"""), " ")
                .trim()

            nameOnly = nameOnly.replace(Regex("""\s+"""), " ")

            if (nameOnly.length >= 2) {
                val priceVal = if (numbers.isNotEmpty()) numbers.last() else 0.0
                val qtyVal = if (numbers.size >= 2) numbers[numbers.size - 2] else 1.0

                resultList.add(ExtractedItem(name = nameOnly, price = priceVal, quantity = qtyVal))
            }
        }
        return resultList
    }

    // خوارزمية الفلترة للتعرف على السطور غير المفيدة
    private fun isNoiseOrHeader(line: String): Boolean {
        if (line.isBlank() || line.length < 3) return true

        val lowerLine = line.lowercase()
        // إذا كان السطر يطابق كلياً أو يحتوي كلمة ترويسة مع انعدام الأرقام
        for (keyword in IGNORED_HEADER_KEYWORDS) {
            if (lowerLine.contains(keyword) && !line.any { it.isDigit() }) {
                return true
            }
        }
        return false
    }
}
