package com.smartassistant.app.data

data class ImportedCustomer(val name: String, val balance: Double)
data class ImportedItem(val name: String, val quantity: Double, val unit: String = "حبة")

object SmartImportEngine {

    fun parseCustomerRow(row: List<String>): ImportedCustomer? {
        val cleanRow = row.map { it.trim() }.filter { it.isNotEmpty() }
        if (cleanRow.isEmpty()) return null

        // استبعاد أسطر العناوين
        if (cleanRow.any { it.contains("اسم") || it.contains("العميل") || it.contains("الرصيد") || it.contains("التسلسل") }) {
            if (cleanRow.all { it.toDoubleOrNull() == null }) return null
        }

        // عزل النصوص والأرقام
        val textCols = cleanRow.filter { it.toDoubleOrNull() == null }
        val numCols = cleanRow.mapNotNull { it.replace(",", "").toDoubleOrNull() }

        if (textCols.isEmpty()) return null

        val customerName = textCols.first()
        // إذا كان هناك أكثر من رقم (مثلاً: رقم تسلسلي + رصيد)، يتم اعتماد الرقم الأخير كالرصيد
        val balance = if (numCols.size > 1) numCols.last() else (numCols.firstOrNull() ?: 0.0)

        return ImportedCustomer(name = customerName, balance = balance)
    }

    fun parseInventoryRow(row: List<String>): ImportedItem? {
        val cleanRow = row.map { it.trim() }.filter { it.isNotEmpty() }
        if (cleanRow.isEmpty()) return null

        if (cleanRow.any { it.contains("الصنف") || it.contains("الكمية") || it.contains("المخزن") }) {
            if (cleanRow.all { it.toDoubleOrNull() == null }) return null
        }

        val textCols = cleanRow.filter { it.toDoubleOrNull() == null }
        val numCols = cleanRow.mapNotNull { it.replace(",", "").toDoubleOrNull() }

        if (textCols.isEmpty()) return null

        val itemName = textCols.first()
        val unitName = if (textCols.size > 1) textCols[1] else "حبة"
        val quantity = if (numCols.size > 1) numCols.last() else (numCols.firstOrNull() ?: 0.0)

        return ImportedItem(name = itemName, quantity = quantity, unit = unitName)
    }
}
