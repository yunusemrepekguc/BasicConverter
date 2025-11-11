package com.yempe.financeapps.feature.converter.data.remote

import com.yempe.financeapps.feature.converter.data.model.AssetModelDto
import retrofit2.http.GET

interface AssetApi {

    @GET("/currencies")
    suspend fun getAssets(): List<AssetModelDto>
}