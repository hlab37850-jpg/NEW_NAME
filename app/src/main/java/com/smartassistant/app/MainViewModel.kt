package com.smartassistant.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<String>("الحالة: جاهز للعمل")
    val uiState: StateFlow<String> = _uiState

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun createExcel(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val file = PoiHelper.createSampleExcelFile(context, "SmartReport.xlsx")
                _uiState.value = "تم إنشاء ملف Excel بنجاح:\n${file.name}"
            } catch (e: Exception) {
                _uiState.value = "خطأ أثناء إنشاء Excel: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createWord(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val file = PoiHelper.createSampleWordFile(context, "SmartDocument.docx")
                _uiState.value = "تم إنشاء مستند Word بنجاح:\n${file.name}"
            } catch (e: Exception) {
                _uiState.value = "خطأ أثناء إنشاء Word: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun processPdfFile(importType: String, rawPdfText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                if (importType == "CUSTOMERS") {
                    val customers = PdfImportEngine.extractCustomers(rawPdfText)
                    val sample = customers.take(3).joinToString("\n") { "• ${it.name} | هاتف: ${it.phone} | رصيد: ${it.balance}" }
                    _uiState.value = "تم استخراج ${customers.size} عميل بنجاح خالي من العناوين والترويسات.\n\nعينات:\n$sample"
                } else {
                    val items = PdfImportEngine.extractItems(rawPdfText)
                    val sample = items.take(3).joinToString("\n") { "• ${it.name} | السعر: ${it.price} | الكمية: ${it.quantity}" }
                    _uiState.value = "تم استخراج ${items.size} صنف بنجاح خالي من العناوين والترويسات.\n\nعينات:\n$sample"
                }
            } catch (e: Exception) {
                _uiState.value = "خطأ في تحليل محتوى PDF: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
