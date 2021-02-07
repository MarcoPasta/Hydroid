package com.luan.hsworms.hydroid.Backend.Database
//TODO: Packages umnennen
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Interface for accessing database WaterRequirement table records (Data Access Object)
 *
 * @author Andrej Alpatov
 */
@Dao
interface WaterRequirementDao {
    /**
     * Adding an entry to a DB table WaterRequirement
     */
    @Insert
    suspend fun insert(waterRequirement: WaterRequirement)

    /**
     * Deleting a record in a WaterRequirement table
     */
    @Delete
    suspend fun delete(waterRequirement: WaterRequirement)

    /**
     * Update WaterRequirement table entry
     */
    @Update
    suspend fun update(waterRequirement: WaterRequirement)

    /**
     * Database query to find a record that matches the search criteria by user weight and gender
     */
    @Query("SELECT requirements FROM WaterRequirement WHERE weight = :weightIn AND genderWoman = :genderIn")
    suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int):Int

    /**
     * Retrieving all records of a WaterRequirement database table
     */
    @Query("SELECT * FROM WaterRequirement")
    suspend fun  getWaterRequirements():List<WaterRequirement>

    /**
     * Getting live data when changing a WaterRequirement table
     */
    @Query("SELECT * FROM WaterRequirement")
    fun getLiveData(): LiveData<List<WaterRequirement>>
}