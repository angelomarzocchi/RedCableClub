package com.oneplus.redcableclub

import android.app.Application
import com.oneplus.redcableclub.data.AppContainer
import com.oneplus.redcableclub.data.DefaultAppContainer

class RedCableClubApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}