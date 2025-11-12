package com.yempe.financeapps.feature.converter

import com.yempe.financeapps.core.database.entity.AssetEntity
import com.yempe.financeapps.feature.converter.data.mapper.AssetMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AssetMapperTest { // UNIT TEST

    private lateinit var assetMapper: AssetMapper

    @Before
    fun setup() {
        assetMapper = AssetMapper()
    }

    @Test
    fun entityToDomainModelShouldMapCorrectly() {
        val assetEntity = AssetEntity(
            code = "USD",
            name = "US Dollar",
            symbol = "$",
            isFavorite = true
        )

        val result = assetMapper.entityToDomainModel(assetEntity)
        assertEquals("USD", result.code)
        assertEquals("US Dollar", result.name)
        assertEquals("$", result.symbol)
        assertTrue(result.isFavorite)
    }

    @Test
    fun entityToDomainModelShouldCoverAllFields() {
        val assetEntity = AssetEntity(
            code = "EUR",
            name = "Euro",
            symbol = "€",
            isFavorite = false
        )
        val result = assetMapper.entityToDomainModel(assetEntity)
        assertEquals(assetEntity.code, result.code)
        assertEquals(assetEntity.name, result.name)
        assertEquals(assetEntity.symbol, result.symbol)
        assertEquals(assetEntity.isFavorite, result.isFavorite)
    }
}