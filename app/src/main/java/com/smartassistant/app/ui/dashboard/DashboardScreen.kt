package com.smartassistant.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartassistant.app.MainViewModel
import com.smartassistant.app.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToCustomers: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToImport: () -> Unit,
    onNavigateToAI: () -> Unit
) {
    val customerCount by viewModel.totalCustomersCount.collectAsState(initial = 0)
    val productCount by viewModel.totalProductsCount.collectAsState(initial = 0)
    val lowStockCount by viewModel.lowStockCount.collectAsState(initial = 0)
    val totalBalance by viewModel.totalBalancesSum.collectAsState(initial = 0.0)
    val shopSettings by viewModel.shopSettings.collectAsState(initial = null)

    Column(modifier = Modifier.fillMaxSize().background(AppBackground).padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = shopSettings?.shopName?.ifEmpty { "المساعد الذكي" } ?: "المساعد الذكي",
                    color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "مرحباً بك 👋 إليك نظرة عامة على نشاطك اليوم", color = Color.LightGray, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            item { StatCard("العملاء", "$customerCount", Icons.Default.People, PrimaryBlue, onNavigateToCustomers) }
            item { StatCard("إجمالي الأرصدة", "${totalBalance ?: 0.0}", Icons.Default.AccountBalanceWallet, GreenSuccess, onNavigateToCustomers) }
            item { StatCard("الأصناف", "$productCount", Icons.Default.Inventory, CyanAccent, onNavigateToInventory) }
            item { StatCard("النواقص", "$lowStockCount", Icons.Default.Warning, RedDanger, onNavigateToInventory) }
            item { StatCard("الاستيراد الذكي", "DB / Excel", Icons.Default.FileUpload, GoldAccent, onNavigateToImport) }
            item { StatCard("المساعد الذكي AI", "نشط", Icons.Default.AutoAwesome, PurpleAI, onNavigateToAI) }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, iconTint: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontSize = 13.sp, color = GrayText)
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
        }
    }
}
