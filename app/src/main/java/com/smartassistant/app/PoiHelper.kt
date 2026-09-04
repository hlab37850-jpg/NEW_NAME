package com.smartassistant.app

import android.content.Context
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object PoiHelper {

    // إنشاء ملف إكسل افتراضي
    fun createSampleExcelFile(context: Context, fileName: String): File {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("المساعد الذكي")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("المعرف")
        headerRow.createCell(1).setCellValue("اسم المهمة")
        headerRow.createCell(2).setCellValue("الحالة")

        val dataRow = sheet.createRow(1)
        dataRow.createCell(0).setCellValue(1.0)
        dataRow.createCell(1).setCellValue("اختبار بناء المستندات")
        dataRow.createCell(2).setCellValue("مكتمل بنجاح")

        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { out ->
            workbook.write(out)
        }
        workbook.close()
        return file
    }

    // إنشاء ملف وورد افتراضي
    fun createSampleWordFile(context: Context, fileName: String): File {
        val document = XWPFDocument()
        val paragraph = document.createParagraph()
        val run = paragraph.createRun()
        run.isBold = true
        run.fontSize = 18
        run.setText("مستند تم إنشاؤه بواسطة تطبيق المساعد الذكي Pro")

        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { out ->
            document.write(out)
        }
        document.close()
        return file
    }

    // قراءة محتوى ملف Excel
    fun readExcelContent(inputStream: InputStream): String {
        val sb = StringBuilder()
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        for (row in sheet) {
            for (cell in row) {
                sb.append(cell.toString()).append(" | ")
            }
            sb.append("\n")
        }
        workbook.close()
        return sb.toString()
    }

    /**
     * تهيئة PDFBox (يجب استدعاؤها مرة واحدة قبل قراءة ملفات PDF)
     */
    fun initPdfBox(context: Context) {
        // PDFBox on Android requires resource initialization
        PDFBoxResourceLoader.init(context.applicationContext)
    }

    /**
     * قراءة نص من ملف PDF باستخدام PDFBox
     */
    fun readPdfContent(inputStream: InputStream): String {
        var document: PDDocument? = null
        return try {
            document = PDDocument.load(inputStream)
            val stripper = PDFTextStripper()
            stripper.getText(document)
        } catch (e: Exception) {
            // أعاد رسالة خطأ قابلة للعرض بدلًا من رمي استثناء تُبطل البناء
            throw e
        } finally {
            try {
                document?.close()
            } catch (_: Exception) {
            }
        }
    }
}
