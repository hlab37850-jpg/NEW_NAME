package com.smartassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_settings")
data class ShopSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val shopName: String = "",
    val logoUri: String? = null,
    val phone: String = "",
    val whatsapp: String = "",
    val address: String = "",
    val currency: String = "YER",
    val lastUpdated: Long = System.currentTimeMillis()
)
