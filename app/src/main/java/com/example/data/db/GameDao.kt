package com.example.data.db

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    // Player Profile
    @Query("SELECT * FROM player_profile WHERE id = 1")
    fun getPlayerProfileFlow(): Flow<PlayerProfileEntity?>

    @Query("SELECT * FROM player_profile WHERE id = 1")
    suspend fun getPlayerProfileDirect(): PlayerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlayerProfile(profile: PlayerProfileEntity)

    // Farm Plots
    @Query("SELECT * FROM farm_plots ORDER BY plotIndex ASC")
    fun getAllPlotsFlow(): Flow<List<PlotEntity>>

    @Query("SELECT * FROM farm_plots ORDER BY plotIndex ASC")
    suspend fun getAllPlotsDirect(): List<PlotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlot(plot: PlotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPlots(plots: List<PlotEntity>)

    @Update
    suspend fun updatePlot(plot: PlotEntity)

    // Livestock
    @Query("SELECT * FROM livestock ORDER BY id ASC")
    fun getAllLivestockFlow(): Flow<List<LivestockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLivestock(animal: LivestockEntity)

    @Update
    suspend fun updateLivestock(animal: LivestockEntity)

    @Query("DELETE FROM livestock WHERE id = :id")
    suspend fun deleteLivestockById(id: Int)

    // Inventory
    @Query("SELECT * FROM inventory")
    fun getAllInventoryFlow(): Flow<List<InventoryEntity>>

    @Query("SELECT * FROM inventory WHERE itemKey = :key")
    suspend fun getInventoryItemDirect(key: String): InventoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateInventoryItem(item: InventoryEntity)

    // Quests
    @Query("SELECT * FROM quests ORDER BY isCompleted ASC, rewardRupees DESC")
    fun getAllQuestsFlow(): Flow<List<QuestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuests(quests: List<QuestEntity>)

    @Update
    suspend fun updateQuest(quest: QuestEntity)
}
