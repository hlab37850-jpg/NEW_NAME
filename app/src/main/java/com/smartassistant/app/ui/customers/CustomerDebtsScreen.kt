package com.smartassistant.app.ui.customers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CustomerDebt(
    val name: String,
    val balance: Double
)

@Composable
fun CustomerDebtsScreen() {
    var customers by remember { mutableStateOf<List<CustomerDebt>>(emptyList()) }

    // محاكاة تحويل صفوف الملف مع تحسين الفهارس
    fun parseImportedRow(row: List<Any>): CustomerDebt? {
        if (row.size < 2) return null
        
        // تعديل الفهارس: 
        // row[0] = التسلسل (يتم تجاهله)
        // row[1] = اسم العميل
        // row[2] = المبلغ/الرصيد
        val name = row.getOrNull(1)?.toString()?.trim() ?: "عميل غير معروف"
        val rawAmount = row.getOrNull(2)?.toString()?.replace(",", ".")?.trim() ?: "0.0"
        val balance = rawAmount.toDoubleOrNull() ?: 0.0

        return CustomerDebt(name = name, balance = balance)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "قائمة العملاء والأرصدة",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = customer.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "YER ${customer.balance}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
