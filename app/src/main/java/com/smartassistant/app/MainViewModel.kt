package com.smartassistant.app

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartassistant.app.data.local.AppDatabase
import com.smartassistant.app.data.local.entity.CustomerEntity
import com.smartassistant.app.data.local.entity.ProductEntity
import com.smartassistant.app.data.local.entity.ShopSettingsEntity
import com.smartassistant.app.data.parser.DataPreserver
import com.smartassistant.app.data.parser.PdfParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AIMessage(val sender: String, val text: String, val timestamp: Long = System.currentTimeMillis())

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val customerDao = db.customerDao()
    private val productDao = db.productDao()
    private val shopSettingsDao = db.shopSettingsDao()

    val customers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    val products: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val shopSettings: Flow<ShopSettingsEntity?> = shopSettingsDao.getShopSettings()

    val totalCustomersCount: Flow<Int> = customerDao.getCustomerCount()
    val totalProductsCount: Flow<Int> = productDao.getProductCount()
    val lowStockCount: Flow<Int> = productDao.getLowStockCount()
    val totalBalancesSum: Flow<Double?> = customerDao.getTotalBalances()

    private val _aiMessages = MutableStateFlow<List<AIMessage>>(
        listOf(AIMessage("ai", "مرحباً بك! أنا مساعدك الذكي. كيف يمكنني مساعدتك في إدارة محلّك اليوم؟"))
    )
    val aiMessages: StateFlow<List<AIMessage>> = _aiMessages.asStateFlow()

    fun addOrUpdateCustomer(name: String, phone: String, balance: Double) {
        viewModelScope.launch {
            customerDao.insertCustomer(CustomerEntity(name = name.trim(), phone = phone.trim(), currentBalance = balance))
        }
    }

    fun addOrUpdateProduct(rawName: String, quantity: Double, minThreshold: Double) {
        viewModelScope.launch {
            val processed = DataPreserver.preserveProductText(rawName)
            productDao.insertProduct(
                ProductEntity(rawName = processed.rawText, parsedName = processed.parsedSearchKey, currentQuantity = quantity, minThreshold = minThreshold)
            )
        }
    }

    fun importCustomersFromPdf(context: Context, uri: Uri, onComplete: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val text = PdfParser.extractTextFromPdf(context, uri)
            val customersList = PdfParser.parseCustomersPdf(text)
            customersList.forEach { customerDao.insertCustomer(it) }
            withContext(Dispatchers.Main) { onComplete(customersList.size) }
        }
    }

    fun importInventoryFromPdf(context: Context, uri: Uri, onComplete: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val text = PdfParser.extractTextFromPdf(context, uri)
            val productsList = PdfParser.parseInventoryPdf(text)
            productsList.forEach { productDao.insertProduct(it) }
            withContext(Dispatchers.Main) { onComplete(productsList.size) }
        }
    }

    fun updateShopSettings(shopName: String, phone: String, whatsapp: String, address: String) {
        viewModelScope.launch {
            shopSettingsDao.updateShopSettings(ShopSettingsEntity(shopName = shopName, phone = phone, whatsapp = whatsapp, address = address))
        }
    }

    fun sendAIMessage(userText: String) {
        if (userText.isBlank()) return
        val currentList = _aiMessages.value.toMutableList()
        currentList.add(AIMessage("user", userText))
        _aiMessages.value = currentList

        viewModelScope.launch {
            val reply = "تم تحليل طلبك بنجاح! يمكنني مساعدتك في تصدير التقارير أو متابعة الأصناف والعملاء."
            val updatedList = _aiMessages.value.toMutableList()
            updatedList.add(AIMessage("ai", reply))
            _aiMessages.value = updatedList
        }
    }
}
