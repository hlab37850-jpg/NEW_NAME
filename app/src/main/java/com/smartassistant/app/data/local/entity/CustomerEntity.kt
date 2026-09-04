package com.smartassistant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "customers",
    indices = [Index(value = ["name"]), Index(value = ["phone"])]
)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalExternalId: String? = null,
    val name: String,
    val phone: String,
    val address: String? = null,
    val currentBalance: Double,
    val currency: String = "YER",
    val dueDate: Long? = null,
    val dueTime: String? = null,
    val isForgotten: Boolean = false,
    val notes: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)
