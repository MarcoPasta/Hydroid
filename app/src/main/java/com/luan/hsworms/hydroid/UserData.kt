package com.luan.hsworms.hydroid

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData (@PrimaryKey val name: String = "",
                var gender: Int = 1,/*female - 1, male - 2*/
                var weight: Double = 0.0,
                var unitOfMeasurement: Int = 1){/*Metric - 1, English - 2*/
}