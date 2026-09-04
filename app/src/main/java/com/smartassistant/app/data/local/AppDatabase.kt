package com.smartassistant.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smartassistant.app.data.local.dao.CustomerDao
import com.smartassistant.app.data.local.dao.ProductDao
import com.smartassistant.app.data.local.dao.ShopSettingsDao
import com.smartassistant.app.data.local.entity.CustomerEntity
import com.smartassistant.app.data.local.entity.ProductEntity
import com.smartassistant.app.data.local.entity.ShopSettingsEntity

@Database(
    entities = [
        CustomerEntity::class,
        ProductEntity::class,
        ShopSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
    abstract fun shopSettingsDao(): ShopSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_assistant_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
