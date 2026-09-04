package com.smartassistant.app

import android.content.Context
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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
}
