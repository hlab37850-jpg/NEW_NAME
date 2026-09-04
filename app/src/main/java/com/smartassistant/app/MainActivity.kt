package com.smartassistant.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartassistant.app.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartAssistantTheme {
                MainAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("المساعد الذكي", fontWeight = FontWeight.Bold, color = SurfaceWhite) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyDark),
                actions = {
                    IconButton(onClick = { /* الإشعارات */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = SurfaceWhite)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = SurfaceWhite) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("الرئيسية") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.People, contentDescription = null) },
                    label = { Text("العملاء") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                    label = { Text("الأصناف") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                    label = { Text("المساعد الذكي") }
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("المزيد") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* إضافة جديد */ },
                containerColor = PrimaryBlue,
                contentColor = SurfaceWhite
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
        ) {
            when (selectedTab) {
                0 -> DashboardContent()
                1 -> SimplePlaceholderScreen("شاشة العملاء وإدارة الأرصدة")
                2 -> SimplePlaceholderScreen("شاشة الأصناف والمخزون")
                3 -> SimplePlaceholderScreen("شاشة المساعد الذكي AI")
                else -> SimplePlaceholderScreen("الإعدادات والاستيراد الذكي")
            }
        }
    }
}

@Composable
fun DashboardContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("مرحباً بك 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NavyDark)
        Text("إليك نظرة عامة على نشاطك اليوم", fontSize = 14.sp, color = GrayText)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { StatCard("إجمالي العملاء", "0", Icons.Default.People, PrimaryBlue) }
            item { StatCard("إجمالي الأرصدة", "0.00 ر.ي", Icons.Default.AccountBalanceWallet, GreenSuccess) }
            item { StatCard("استحقاقات اليوم", "0", Icons.Default.Event, GoldAccent) }
            item { StatCard("العملاء المنسيون", "0", Icons.Default.Warning, RedDanger) }
            item { StatCard("إجمالي الأصناف", "0", Icons.Default.Inventory2, CyanAccent) }
            item { StatCard("الأصناف الناقصة", "0", Icons.Default.TrendingDown, RedDanger) }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 12.sp, color = GrayText)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
        }
    }
}

@Composable
fun SimplePlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = NavyDark)
    }
}
