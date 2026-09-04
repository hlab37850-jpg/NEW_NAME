package com.smartassistant.app.ui.ai_assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smartassistant.app.MainViewModel
import com.smartassistant.app.ui.theme.*

@Composable
fun AIAssistantScreen(viewModel: MainViewModel) {
    val messages by viewModel.aiMessages.collectAsState()
    var textInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(AppBackground).padding(16.dp)) {
        Text(text = "المساعد الذكي AI", color = PurpleAI)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { msg ->
                Text(text = "${msg.sender}: ${msg.text}", modifier = Modifier.padding(8.dp))
            }
        }
        Row {
            OutlinedTextField(value = textInput, onValueChange = { textInput = it }, modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.sendAIMessage(textInput); textInput = "" }) { Text("إرسال") }
        }
    }
}
