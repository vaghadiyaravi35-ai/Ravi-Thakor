package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Core Player Game Profile persisted in Room Database
 */
@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "किसान भैया",
    val rupees: Long = 2500L,
    val exp: Int = 0,
    val level: Int = 1,
    val currentDay: Int = 1,
    val currentSeason: String = "SURAJ_GARMI", // SURAJ_GARMI (Summer), BARISH_MONSOON, SARDI_WINTER
    val currentWeather: String = "SUNNY", // SUNNY, RAINY, CLOUDY, HEATWAVE
    val houseLevel: Int = 1, // 1: Kuccha Hut, 2: Brick House, 3: Haveli, 4: Smart Farmhouse
    val hasTubewell: Boolean = false,
    val hasSolarPump: Boolean = false,
    val storageCapacityKg: Int = 500,
    val activeTractor: String = "DESI_HAL", // DESI_HAL, SWARAJ_744, MAHINDRA_575, THRESHER_X, COMBINE_PRO
    val dieselLiters: Float = 10.0f
)

/**
 * Land Plot for Farming
 */
@Entity(tableName = "farm_plots")
data class PlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plotIndex: Int,
    val cropType: String = "NONE", // NONE, WHEAT, BAJRA, COTTON, PADDY, VEGETABLES
    val growthStage: Int = 0, // 0: Empty, 1: Seeded, 2: Growing, 3: Mature/Ready, 4: Withered
    val isWatered: Boolean = false,
    val isFertilized: Boolean = false,
    val isPlowed: Boolean = false,
    val plantedTimeMs: Long = 0L,
    val growthProgressPct: Float = 0f // 0 to 100%
)

/**
 * Livestock animal in Dairy Shed
 */
@Entity(tableName = "livestock")
data class LivestockEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val animalType: String, // COW, BUFFALO, GOAT
    val nickname: String,
    val healthPct: Int = 100,
    val isFedToday: Boolean = false,
    val milkReadyLiters: Float = 0f,
    val totalMilkProduced: Float = 0f
)

/**
 * Inventory items stored in Silo/Home
 */
@Entity(tableName = "inventory")
data class InventoryEntity(
    @PrimaryKey val itemKey: String, // WHEAT, BAJRA, COTTON, PADDY, VEGETABLES, MILK, FISH, FODDER, SEED_WHEAT, etc.
    val itemNameHindi: String,
    val quantity: Int
)

/**
 * Quests / Missions given by NPC Villagers
 */
@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey val questId: String,
    val titleHindi: String,
    val titleEng: String,
    val npcName: String,
    val npcRole: String,
    val descriptionHindi: String,
    val targetType: String, // SELL_CROP, MILKING, CATCH_FISH, BUY_TRACTOR, HARVEST_PADDY
    val targetAmount: Int,
    val currentAmount: Int = 0,
    val rewardRupees: Long,
    val rewardExp: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false
)
