package com.yempe.financeapps.feature.converter.data.di

import com.yempe.financeapps.core.domain.repository.asset.AssetRepository
import com.yempe.financeapps.feature.converter.data.repository.AssetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAssetRepository(impl: AssetRepositoryImpl): AssetRepository
}