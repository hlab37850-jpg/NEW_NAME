package com.smartassistant.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val currentBalance: Double, // الرصيد الحالي فقط (إيجابي أو سلبي)
    val rawText: String = "",    // لحفظ البيانات الأصلية بدون تعديل (Zero Data Loss)
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,            // الاسم الكامل بدون حذف أي حروف/مقاسات
    val category: String,
    val unit: String,
    val quantity: Double,
    val minQuantity: Double,
    val rawText: String = ""
)

@Entity(tableName = "due_dates")
data class DueDate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val amount: Double,
    val dueDateMillis: Long,
    val isNotified: Boolean = false,
    val status: String = "PENDING" // PENDING, OVERDUE, COMPLETED
)

@Entity(tableName = "shop_settings")
data class ShopSettings(
    @PrimaryKey val id: Int = 1,
    val shopName: String,
    val phone: String,
    val whatsapp: String,
    val address: String,
    val logoPath: String = ""
)
