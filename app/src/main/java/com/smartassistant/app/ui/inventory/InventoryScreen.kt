package com.smartassistant.app.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartassistant.app.MainViewModel
import com.smartassistant.app.ui.theme.*

@Composable
fun InventoryScreen(viewModel: MainViewModel) {
    val productsList by viewModel.products.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = PrimaryBlue, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "إضافة")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(AppBackground).padding(16.dp)) {
            Text(text = "المخزون والأصناف (الاسم الخام بالكامل)", fontSize = 18.sp, color = NavyDark)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(productsList) { product ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = product.rawName, modifier = Modifier.weight(1f))
                            Text(text = "الكمية: ${product.currentQuantity}", color = PrimaryBlue)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var rawName by remember { mutableStateOf("") }
        var qtyText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("إضافة صنف") },
            text = {
                Column {
                    OutlinedTextField(value = rawName, onValueChange = { rawName = it }, label = { Text("اسم الصنف كاملاً والرموز") })
                    OutlinedTextField(value = qtyText, onValueChange = { qtyText = it }, label = { Text("الكمية") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addOrUpdateProduct(rawName, qtyText.toDoubleOrNull() ?: 0.0, 5.0)
                    showAddDialog = false
                }) { Text("حفظ") }
            }
        )
    }
}
