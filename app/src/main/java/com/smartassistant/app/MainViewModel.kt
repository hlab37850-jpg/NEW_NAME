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
}
