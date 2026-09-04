package com.smartassistant.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.smartassistant.app.ui.ai_assistant.AIAssistantScreen
import com.smartassistant.app.ui.customers.CustomersScreen
import com.smartassistant.app.ui.dashboard.DashboardScreen
import com.smartassistant.app.ui.import_engine.ImportEngineScreen
import com.smartassistant.app.ui.inventory.InventoryScreen
import com.smartassistant.app.ui.settings.SettingsScreen
import com.smartassistant.app.ui.theme.SmartAssistantTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartAssistantTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    var currentTab by remember { mutableIntStateOf(0) }
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(selected = currentTab == 0, onClick = { currentTab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("الرئيسية") })
                                NavigationBarItem(selected = currentTab == 1, onClick = { currentTab = 1 }, icon = { Icon(Icons.Default.People, null) }, label = { Text("العملاء") })
                                NavigationBarItem(selected = currentTab == 2, onClick = { currentTab = 2 }, icon = { Icon(Icons.Default.Inventory, null) }, label = { Text("الأصناف") })
                                NavigationBarItem(selected = currentTab == 3, onClick = { currentTab = 3 }, icon = { Icon(Icons.Default.FileUpload, null) }, label = { Text("الاستيراد") })
                                NavigationBarItem(selected = currentTab == 4, onClick = { currentTab = 4 }, icon = { Icon(Icons.Default.AutoAwesome, null) }, label = { Text("AI") })
                            }
                        }
                    ) { padding ->
                        Surface(modifier = Modifier.padding(padding)) {
                            when (currentTab) {
                                0 -> DashboardScreen(viewModel, { currentTab = 1 }, { currentTab = 2 }, { currentTab = 3 }, { currentTab = 4 })
                                1 -> CustomersScreen(viewModel)
                                2 -> InventoryScreen(viewModel)
                                3 -> ImportEngineScreen(viewModel)
                                4 -> AIAssistantScreen(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
