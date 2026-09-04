package com.smartassistant.app

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartassistant.app.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.tvStatus.text = "ميزة قراءة واختيار الملفات جاهزة ومتاحة للمستندات."
        }
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
            }
        }
    }
}
