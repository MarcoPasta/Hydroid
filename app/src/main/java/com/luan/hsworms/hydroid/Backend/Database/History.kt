package com.luan.hsworms.hydroid.Backend.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var date: String,
    var drunk: Int,
    var requirements: Int,
    var fulfillment: Int,
    var weight: Int
)
