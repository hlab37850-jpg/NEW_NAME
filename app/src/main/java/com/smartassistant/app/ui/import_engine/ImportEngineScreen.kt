package com.smartassistant.app.ui.import_engine

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartassistant.app.MainViewModel
import com.smartassistant.app.ui.theme.*

@Composable
fun ImportEngineScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var isImporting by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("") }

    val customerPdfLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            isImporting = true
            statusMessage = "جاري قراءة وتحليل ملف العملاء..."
            viewModel.importCustomersFromPdf(context, it) { count ->
                isImporting = false
                statusMessage = "تم استيراد $count عميل بنجاح!"
            }
        }
    }

    val inventoryPdfLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            isImporting = true
            statusMessage = "جاري قراءة وتحليل ملف الأصناف والمخزون..."
            viewModel.importInventoryFromPdf(context, it) { count ->
                isImporting = false
                statusMessage = "تم استيراد $count صنف بنجاح!"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "محرك الاستيراد الذكي (PDF)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NavyDark)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "اختر ملف PDF الخاض بملف العملاء أو المخزون ليتم استخراج البيانات وتخزينها تلقائياً", fontSize = 13.sp, color = GrayText)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "استيراد ملف العملاء (PDF)", fontWeight = FontWeight.Bold, color = NavyDark)
                        Text(text = "استخراج أسماء العملاء، أرقام الهواتف، والأرصدة", fontSize = 12.sp, color = GrayText)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { customerPdfLauncher.launch("application/pdf") },
                    enabled = !isImporting,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("اختر ملف PDF العملاء")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "استيراد ملف الأصناف (PDF)", fontWeight = FontWeight.Bold, color = NavyDark)
                        Text(text = "استخراج اسم الصنف الخام الكامل والكميات", fontSize = 12.sp, color = GrayText)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { inventoryPdfLauncher.launch("application/pdf") },
                    enabled = !isImporting,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("اختر ملف PDF الأصناف", color = NavyDark)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isImporting) {
            CircularProgressIndicator(color = PrimaryBlue)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (statusMessage.isNotEmpty()) {
            Text(text = statusMessage, fontWeight = FontWeight.Bold, color = if (statusMessage.contains("نجاح")) GreenSuccess else NavyDark)
        }
    }
}
