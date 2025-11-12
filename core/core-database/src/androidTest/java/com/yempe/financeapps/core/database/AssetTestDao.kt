package com.yempe.financeapps.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.yempe.financeapps.core.database.dao.AssetDao
import com.yempe.financeapps.core.database.entity.AssetEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class AssetTestDao { // ANDROID TEST

    private lateinit var database: ConverterDatabase
    private lateinit var dao: AssetDao

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            ConverterDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.assetDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetAssets_shouldReturnInsertedData() = runBlocking {
        val asset = AssetEntity(
            code = "USD",
            name = "US Dollar",
            symbol = "test",
            isFavorite = false
        )
        dao.insertAssets(listOf(asset))

        val result = dao.getAllAssets().first()
        assertEquals(1, result.size)
        assertEquals("USD", result.first().code)
        assertEquals("test", result.first().symbol)
    }

    @Test
    fun updateAndGetAssets_shouldReturnUpdatedData() = runBlocking {
        insertAndGetAssets_shouldReturnInsertedData()
        val asset = AssetEntity(
            code = "USD",
            name = "USD Dollar",
            symbol = "test",
            isFavorite = false
        )
        dao.updateAsset(asset)
        val result = dao.getAllAssets().first()
        assertEquals("USD Dollar", result.first().name)
    }
}