package com.yempe.financeapps.core.database.di

import android.content.Context
import androidx.room.Room
import com.yempe.financeapps.core.database.ConverterDatabase
import com.yempe.financeapps.core.database.dao.AssetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ConverterDatabase =
        Room.databaseBuilder(
            context = context,
            klass = ConverterDatabase::class.java,
            name = ConverterDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideAssetDao(
        database: ConverterDatabase
    ): AssetDao = database.assetDao()
}