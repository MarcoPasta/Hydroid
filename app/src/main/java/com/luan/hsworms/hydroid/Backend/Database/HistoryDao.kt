package com.luan.hsworms.hydroid.Backend.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Update
    suspend fun update(history: History)

    @Query("SELECT * FROM History WHERE date = :dateIn")
    suspend fun getHistoryByDate(dateIn: String): History

    @Query("SELECT * FROM History")
    suspend fun getHistoryAll(): List<History>

    @Query("SELECT * FROM History")
    fun getLiveData(): LiveData<List<History>>
}