package com.smartassistant.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartassistant.app.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    var shopName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = shopName, onValueChange = { shopName = it }, label = { Text("اسم المحل") })
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("الهاتف") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.updateShopSettings(shopName, phone, "", "") }) { Text("حفظ الإعدادات") }
    }
}
