package com.smartassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "products",
    indices = [Index(value = ["rawName"]), Index(value = ["productCode"])]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rawName: String,
    val parsedName: String,
    val productCode: String? = null,
    val category: String? = null,
    val unit: String? = null,
    val currentQuantity: Double,
    val minThreshold: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)
