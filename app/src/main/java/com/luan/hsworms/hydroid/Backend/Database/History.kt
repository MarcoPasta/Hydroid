package com.luan.hsworms.hydroid.Backend.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for table History
 */
@Entity
data class History(
    /**
     * Auto-generated index
     */
    @PrimaryKey(autoGenerate = true) var id: Long,
    /**
     * Date of record
     */
    var date: String,
    /**
     * The amount of water drunk
     */
    var drunk: Int,
    /**
     * User's daily water requirement
     */
    var requirements: Int,
    /**
     * Percentage of daily target
     */
    var fulfillment: Int,
    /**
     * User weight
     */
    var weight: Int
)
