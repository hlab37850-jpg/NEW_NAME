package com.smartassistant.app.data.parser

object DataPreserver {
    data class ProcessedText(val rawText: String, val parsedSearchKey: String)

    fun preserveProductText(rawInput: String): ProcessedText {
        val trimmed = rawInput.trim()
        val searchKey = trimmed.lowercase()
            .replace("أ", "ا").replace("إ", "ا")
            .replace("آ", "ا").replace("ة", "ه")
        return ProcessedText(rawText = trimmed, parsedSearchKey = searchKey)
    }
}
