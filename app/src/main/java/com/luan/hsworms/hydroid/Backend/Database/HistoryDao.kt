package com.luan.hsworms.hydroid.Backend.Database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Interface for accessing database History table records (Data Access Object)
 *
 * @author Andrej Alpatov
 */
@Dao
interface HistoryDao {
    /**
     * Adding an entry to a DB table History
     */
    @Insert
    suspend fun insert(history: History)

    /**
     * Deleting a record in a History table
     */
    @Delete
    suspend fun delete(history: History)

    /**
     * Update History table entry
     */
    @Update
    suspend fun update(history: History)

    /**
     * Database query to find a record that matches the search criteria by date
     */
    @Query("SELECT * FROM History WHERE date = :dateIn")
    suspend fun getHistoryByDate(dateIn: String): History

    /**
     * Retrieving all records of a History database table
     */
    @Query("SELECT * FROM History")
    suspend fun getHistoryAll(): List<History>

    /**
     * Getting live data when changing a table
     */
    @Query("SELECT * FROM History")
    fun getLiveData(): LiveData<List<History>>
}