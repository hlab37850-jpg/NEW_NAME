package com.smartassistant.app.data.parser

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import com.smartassistant.app.data.local.entity.CustomerEntity
import com.smartassistant.app.data.local.entity.ProductEntity

object PdfParser {

    fun extractTextFromPdf(context: Context, uri: Uri): String {
        return try {
            PDFBoxResourceLoader.init(context)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                PDDocument.load(inputStream).use { document ->
                    val stripper = PDFTextStripper()
                    stripper.getText(document)
                }
            } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun parseCustomersPdf(text: String): List<CustomerEntity> {
        val customers = mutableListOf<CustomerEntity>()
        val lines = text.split("\n", "\r")

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isBlank() || trimmed.contains("الاسم") || trimmed.contains("الرصيد")) continue

            val parts = trimmed.split(Regex("\\s+"))
            if (parts.size >= 2) {
                val numbers = parts.filter { it.matches(Regex("^-?\\d+(\\.\\d+)?$")) }
                val names = parts.filter { !it.matches(Regex("^-?\\d+(\\.\\d+)?$")) }

                if (names.isNotEmpty()) {
                    val name = names.joinToString(" ")
                    val phone = numbers.firstOrNull { it.length in 7..14 } ?: ""
                    val balance = numbers.firstOrNull { it != phone }?.toDoubleOrNull() ?: 0.0

                    customers.add(CustomerEntity(name = name, phone = phone, currentBalance = balance))
                }
            }
        }
        return customers
    }

    fun parseInventoryPdf(text: String): List<ProductEntity> {
        val products = mutableListOf<ProductEntity>()
        val lines = text.split("\n", "\r")

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isBlank() || trimmed.contains("الصنف") || trimmed.contains("الكمية")) continue

            val parts = trimmed.split(Regex("\\s+"))
            if (parts.isNotEmpty()) {
                val numberIndex = parts.indexOfLast { it.matches(Regex("^\\d+(\\.\\d+)?$")) }
                if (numberIndex != -1) {
                    val rawName = parts.subList(0, numberIndex).joinToString(" ")
                    val qty = parts[numberIndex].toDoubleOrNull() ?: 0.0

                    if (rawName.isNotBlank()) {
                        val processed = DataPreserver.preserveProductText(rawName)
                        products.add(
                            ProductEntity(
                                rawName = processed.rawText,
                                parsedName = processed.parsedSearchKey,
                                currentQuantity = qty,
                                minThreshold = 5.0
                            )
                        )
                    }
                }
            }
        }
        return products
    }
}
