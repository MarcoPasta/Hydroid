package com.luan.hsworms.hydroid.Backend.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WaterRequirementDao {
    @Insert
    suspend fun insert(waterRequirement: WaterRequirement)

    @Delete
    suspend fun delete(waterRequirement: WaterRequirement)

    @Update
    suspend fun update(waterRequirement: WaterRequirement)

    @Query("SELECT requirements FROM WaterRequirement WHERE weight = :weightIn AND genderWoman = :genderIn")
    suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int):Int

    @Query("SELECT * FROM WaterRequirement")
    suspend fun  getWaterRequirements():List<WaterRequirement>

    @Query("SELECT * FROM WaterRequirement")
    fun getLiveData(): LiveData<List<WaterRequirement>>
}