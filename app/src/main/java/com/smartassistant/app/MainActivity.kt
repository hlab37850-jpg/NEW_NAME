package com.smartassistant.app

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smartassistant.app.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var selectedImportType: String = "CUSTOMERS"

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val rawText = PoiHelper.readPdfContent(inputStream)
                    viewModel.processPdfFile(selectedImportType, rawText)
                }
            } catch (e: Exception) {
                binding.tvStatus.text = "فشل في فتح وتطبيق المحرك على الملف: ${e.localizedMessage}"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PoiHelper.initPdfBox(applicationContext)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnCreateExcel.setOnClickListener {
            viewModel.createExcel(this)
        }

        binding.btnCreateWord.setOnClickListener {
            viewModel.createWord(this)
        }

        binding.btnProcessFile.setOnClickListener {
            showImportSelectionDialog()
        }
    }

    private fun showImportSelectionDialog() {
        val options = arrayOf("استيراد كشف العملاء (تصفية واستخراج تلقائي)", "استيراد كشف الأصناف (تصفية واستخراج تلقائي)")
        MaterialAlertDialogBuilder(this)
            .setTitle("حدد نوع البيانات المراد تحليلها")
            .setItems(options) { _, which ->
                selectedImportType = if (which == 0) "CUSTOMERS" else "ITEMS"
                filePickerLauncher.launch("application/pdf")
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { status ->
                binding.tvStatus.text = status
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { loading ->
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
                binding.btnCreateExcel.isEnabled = !loading
                binding.btnCreateWord.isEnabled = !loading
                binding.btnProcessFile.isEnabled = !loading
            }
        }
    }
}
