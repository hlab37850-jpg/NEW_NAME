package com.smartassistant.app.data.local.dao

import androidx.room.*
import com.smartassistant.app.data.local.entity.ShopSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopSettingsDao {
    @Query("SELECT * FROM shop_settings WHERE id = 1 LIMIT 1")
    fun getShopSettings(): Flow<ShopSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateShopSettings(settings: ShopSettingsEntity)
}
