package com.smartassistant.app.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class InventoryItem(
    val id: Int,
    val name: String,
    val currentStock: Int,
    val minStockLimit: Int,
    val suggestedOrder: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartInventoryScreen() {
    val inventoryList = remember {
        listOf(
            InventoryItem(1, "إسمنت مقاوم", 5, 20, 50),
            InventoryItem(2, "أنبوب سباكة 1/2 إنش", 12, 30, 100),
            InventoryItem(3, "مفتاح كهرباء مزدوج", 8, 15, 40)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("النواقص والتزويد الذكي") },
                actions = {
                    Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.padding(8.dp))
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
            items(inventoryList) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                            Badge(containerColor = MaterialTheme.colorScheme.error) {
                                Text("المتبقي: ${item.currentStock}", modifier = Modifier.padding(4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "الكمية المقترحة للطلب: ${item.suggestedOrder} قطعة",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
