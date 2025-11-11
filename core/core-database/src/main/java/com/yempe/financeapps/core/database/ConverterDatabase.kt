package com.yempe.financeapps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yempe.financeapps.core.database.dao.AssetDao
import com.yempe.financeapps.core.database.entity.AssetEntity

@Database(
    entities = [
        AssetEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class ConverterDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao

    companion object {
        const val DATABASE_NAME = "converterDB"
    }
}