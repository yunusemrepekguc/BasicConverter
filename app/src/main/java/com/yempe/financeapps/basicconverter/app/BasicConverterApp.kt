package com.yempe.financeapps.basicconverter.app

import android.app.Application
import com.yempe.financeapps.basicconverter.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BasicConverterApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeLibraries()
    }

    private fun initializeLibraries() {

        if (BuildConfig.DEBUG) {
            Timber.Forest.plant()
        }
    }
}