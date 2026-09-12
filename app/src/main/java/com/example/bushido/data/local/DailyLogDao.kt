package com.example.bushido.data.local

import androidx.room.*
import com.example.bushido.data.model.DailyLog
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyLogDao {
    @Query("SELECT * FROM daily_logs ORDER BY date ASC")
    fun getAllLogs(): Flow<List<DailyLog>>

    @Query("SELECT * FROM daily_logs WHERE cycleId = :cycleId ORDER BY date ASC")
    fun getLogsForCycle(cycleId: String): Flow<List<DailyLog>>

    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    suspend fun getLogByDate(date: String): DailyLog?

    @Query("SELECT * FROM daily_logs WHERE cycleId = :cycleId AND date = :date LIMIT 1")
    suspend fun getLogByCycleAndDate(cycleId: String, date: String): DailyLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DailyLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<DailyLog>)

    @Update
    suspend fun updateLog(log: DailyLog)

    @Delete
    suspend fun deleteLog(log: DailyLog)

    @Query("DELETE FROM daily_logs WHERE cycleId = :cycleId")
    suspend fun deleteLogsForCycle(cycleId: String)

    @Query("DELETE FROM daily_logs")
    suspend fun clearAll()
}
