package com.yempe.financeapps.feature.converter.data.di

import com.yempe.financeapps.feature.converter.data.remote.AssetApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAssetApi(retrofit: Retrofit): AssetApi = retrofit.create(AssetApi::class.java)


}