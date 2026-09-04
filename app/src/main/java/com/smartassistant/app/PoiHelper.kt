package com.smartassistant.app

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object PoiHelper {

    /**
     * تهيئة مكتبة PDFBox للأندرويد
     */
    fun initPdfBox(context: Context) {
        try {
            PDFBoxResourceLoader.init(context.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * قراءة واستخراج النصوص من ملف PDF
     */
    fun readPdfContent(inputStream: InputStream): String {
        var document: PDDocument? = null
        return try {
            document = PDDocument.load(inputStream)
            val stripper = PDFTextStripper()
            stripper.getText(document)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        } finally {
            try {
                document?.close()
            } catch (_: Exception) {
            }
        }
    }

    // --- توليد ملفات Excel بكافة الأنماط ---

    fun createSampleExcelFile(outputStream: OutputStream) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")
        val row = sheet.createRow(0)
        row.createCell(0).setCellValue("نموذج بيانات Excel")
        workbook.write(outputStream)
        workbook.close()
    }

    fun createSampleExcelFile(file: File): File {
        FileOutputStream(file).use { os -> createSampleExcelFile(os) }
        return file
    }

    fun createSampleExcelFile(filePath: String): File {
        return createSampleExcelFile(File(filePath))
    }

    fun createSampleExcelFile(context: Context): File {
        val file = File(context.cacheDir, "sample.xlsx")
        return createSampleExcelFile(file)
    }

    fun createSampleExcelFile(): ByteArray {
        val bos = ByteArrayOutputStream()
        createSampleExcelFile(bos)
        return bos.toByteArray()
    }

    // --- توليد ملفات Word بكافة الأنماط ---

    fun createSampleWordFile(outputStream: OutputStream) {
        val document = XWPFDocument()
        val paragraph = document.createParagraph()
        val run = paragraph.createRun()
        run.setText("نموذج مستند Word")
        document.write(outputStream)
        document.close()
    }

    fun createSampleWordFile(file: File): File {
        FileOutputStream(file).use { os -> createSampleWordFile(file) }
        return file
    }

    fun createSampleWordFile(filePath: String): File {
        return createSampleWordFile(File(filePath))
    }

    fun createSampleWordFile(context: Context): File {
        val file = File(context.cacheDir, "sample.docx")
        return createSampleWordFile(file)
    }

    fun createSampleWordFile(): ByteArray {
        val bos = ByteArrayOutputStream()
        createSampleWordFile(bos)
        return bos.toByteArray()
    }
}
