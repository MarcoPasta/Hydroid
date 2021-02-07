package com.luan.hsworms.hydroid.backend.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for table WaterRequirement
 *
 * @author Andrej Alpatov
*/
@Entity
data class WaterRequirement(
    /**
     * Auto-generated index
     */
    @PrimaryKey(autoGenerate = true) var idReq:Long,
    /**
     * User gender
     */
    var genderWoman:Boolean,
    /**
     * User weight
     */
    var weight:Int,
    /**
     * User's daily water requirement
     */
    var requirements:Int
)
