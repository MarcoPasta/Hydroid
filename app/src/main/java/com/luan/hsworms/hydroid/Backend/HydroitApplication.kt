package com.luan.hsworms.hydroid.Backend

import android.app.Application
import com.luan.hsworms.hydroid.Backend.Database.HydroidRepository

class HydroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        HydroidRepository.initialize(this)
    }
}