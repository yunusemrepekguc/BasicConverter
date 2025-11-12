package com.yempe.financeapps.feature.converter.data.mapper

import com.yempe.financeapps.core.database.entity.AssetEntity
import com.yempe.financeapps.core.domain.model.AssetModel
import com.yempe.financeapps.feature.converter.data.model.AssetModelDto
import javax.inject.Inject

class AssetMapper @Inject constructor() {

    fun entityToDomainModel(assetEntity: AssetEntity): AssetModel {
        return AssetModel(
            code = assetEntity.code,
            name = assetEntity.name,
            symbol = assetEntity.symbol,
            isFavorite = assetEntity.isFavorite
        )
    }

    fun dtoToEntityModel(dto: AssetModelDto): AssetEntity {
        return AssetEntity(
            code = dto.code,
            name = dto.name,
            symbol = dto.symbol,
            isFavorite = dto.isFavorite // initially false
        )
    }

}