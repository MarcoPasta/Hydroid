package com.luan.hsworms.hydroid.Backend.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaterRequirement(
    @PrimaryKey(autoGenerate = true) var idReq:Long,
    var genderWoman:Boolean,
    var weight:Int,
    var rquirements:Int
)
