package com.yempe.financeapps.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yempe.financeapps.core.database.entity.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    // GET
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(assetEntity: AssetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assetEntities: List<AssetEntity>)

    // READ
    @Query("SELECT * FROM asset WHERE code =:assetCode")
    fun getAssetByCode(assetCode: String): AssetEntity

    @Query("SELECT * FROM asset")
    fun getAllAssets(): Flow<List<AssetEntity>>

    //Update
    @Query("UPDATE ASSET SET is_favorite = :isFavorite WHERE code = :assetCode")
    fun updateAssetFavoriteState(assetCode: String, isFavorite: Boolean)

    //Delete
    @Delete
    fun hardDeleteAllAssetByCode(assetEntity: AssetEntity)

    @Query("DELETE FROM asset")
    fun hardDeleteAllAsset()
}