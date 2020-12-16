package com.luan.hsworms.hydroid.Backend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData (@PrimaryKey val userID: Int = 1,/**/
                     val name: String = "",
                     var gender: Int = 1,/*female - 1, male - 2*/
                     var weight: Double = 0.0){
}