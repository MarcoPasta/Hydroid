package com.luan.hsworms.hydroid.Backend

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface HydroitDao {
    @Query("SELECT name FROM UserData ORDER BY ROWID ASC LIMIT 1")
    fun getUserName(): LiveData<String>
}