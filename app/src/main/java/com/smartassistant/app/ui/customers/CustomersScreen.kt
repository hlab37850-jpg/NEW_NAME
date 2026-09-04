package com.smartassistant.app.ui.customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartassistant.app.MainViewModel
import com.smartassistant.app.data.local.entity.CustomerEntity
import com.smartassistant.app.ui.theme.*

@Composable
fun CustomersScreen(viewModel: MainViewModel) {
    val customersList by viewModel.customers.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = PrimaryBlue, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "إضافة")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(AppBackground).padding(16.dp)) {
            Text(text = "قائمة العملاء والأرصدة", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NavyDark)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(customersList) { customer ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = customer.name, fontWeight = FontWeight.Bold)
                                Text(text = customer.phone, color = GrayText, fontSize = 12.sp)
                            }
                            Text(text = "${customer.currentBalance} YER", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var balText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("إضافة عميل") },
            text = {
                Column {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("الاسم") })
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("الهاتف") })
                    OutlinedTextField(value = balText, onValueChange = { balText = it }, label = { Text("الرصيد") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addOrUpdateCustomer(name, phone, balText.toDoubleOrNull() ?: 0.0)
                    showAddDialog = false
                }) { Text("حفظ") }
            }
        )
    }
}
