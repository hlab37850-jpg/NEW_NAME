package com.smartassistant.app

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.InputStream
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object PoiHelper {

    /**
     * تهيئة مكتبة PDFBox للأندرويد
     */
    fun initPdfBox(context: Context) {
        PDFBoxResourceLoader.init(context.applicationContext)
    }

    /**
     * قراءة ونزع النصوص من ملف PDF
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
