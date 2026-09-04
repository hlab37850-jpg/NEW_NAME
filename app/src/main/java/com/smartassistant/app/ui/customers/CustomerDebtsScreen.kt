package com.smartassistant.app.ui.customers

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class CustomerDebt(
    val id: Int,
    val name: String,
    val phone: String,
    val balance: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDebtsScreen(
    onExportPdf: (CustomerDebt) -> Unit = {}
) {
    val context = LocalContext.current
    val customers = remember {
        listOf(
            CustomerDebt(1, "أحمد علي", "770000000", 15000.0),
            CustomerDebt(2, "محمد سعيد", "730000000", 8500.0),
            CustomerDebt(3, "خالد عمر", "710000000", 42000.0)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إدارة العملاء والمستحقات") },
                actions = {
                    Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.padding(8.dp))
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(customers) { customer ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = customer.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "المتبقي: ${customer.balance} ر.ي", color = MaterialTheme.colorScheme.primary)
                        }

                        Row {
                            IconButton(onClick = { onExportPdf(customer) }) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = "تصدير PDF", tint = MaterialTheme.colorScheme.error)
                            }
                            IconButton(onClick = {
                                val message = "عزيزي ${customer.name}، نود تذكيركم بأن إجمالي المبالغ المتبقية عليكم هو ${customer.balance} ر.ي."
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://api.whatsapp.com/send?phone=${customer.phone}&text=${Uri.encode(message)}")
                                }
                                context.startActivity(intent)
                            }) {
                                Icon(Icons.Default.Send, contentDescription = "تذكير واتساب", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
