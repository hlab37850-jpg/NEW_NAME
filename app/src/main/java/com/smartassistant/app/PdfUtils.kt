package com.smartassistant.app

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream

/**
 * تهيئة مكتبة PDFBox للعمل على أجهزة الأندرويد
 */
fun initPdfBox(context: Context) {
    PDFBoxResourceLoader.init(context)
}

/**
 * قراءة النص المخزن داخل ملف PDF من InputStream
 */
fun readPdfContent(inputStream: InputStream): String {
    return try {
        val document = PDDocument.load(inputStream)
        val stripper = PDFTextStripper()
        val extractedText = stripper.getText(document)
        document.close()
        extractedText
    } catch (e: Exception) {
        e.printStackTrace()
        "خطأ أثناء قراءة ملف PDF: ${e.localizedMessage}"
    }
}
