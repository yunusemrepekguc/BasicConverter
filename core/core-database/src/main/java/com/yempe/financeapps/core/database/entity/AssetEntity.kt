package com.yempe.financeapps.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asset")
data class AssetEntity(

    @PrimaryKey
    @ColumnInfo("code")
    val code: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("symbol")
    val symbol: String,

    @ColumnInfo("is_favorite")
    val isFavorite: Boolean
)
