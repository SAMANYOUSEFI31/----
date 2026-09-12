package com.example.bushido.data.local

import androidx.room.*
import com.example.bushido.data.model.Cycle
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycles ORDER BY startDate DESC")
    fun getAllCycles(): Flow<List<Cycle>>

    @Query("SELECT * FROM cycles WHERE id = :id LIMIT 1")
    suspend fun getCycleById(id: String): Cycle?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycle(cycle: Cycle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycles(cycles: List<Cycle>)

    @Update
    suspend fun updateCycle(cycle: Cycle)

    @Delete
    suspend fun deleteCycle(cycle: Cycle)

    @Query("DELETE FROM cycles WHERE id = :id")
    suspend fun deleteCycleById(id: String)

    @Query("DELETE FROM cycles")
    suspend fun clearAll()
}
