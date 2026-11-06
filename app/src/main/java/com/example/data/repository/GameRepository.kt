package com.example.data.repository

import com.example.data.db.GameDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class GameRepository(private val gameDao: GameDao) {

    val playerProfileFlow: Flow<PlayerProfileEntity?> = gameDao.getPlayerProfileFlow()
    val allPlotsFlow: Flow<List<PlotEntity>> = gameDao.getAllPlotsFlow()
    val allLivestockFlow: Flow<List<LivestockEntity>> = gameDao.getAllLivestockFlow()
    val allInventoryFlow: Flow<List<InventoryEntity>> = gameDao.getAllInventoryFlow()
    val allQuestsFlow: Flow<List<QuestEntity>> = gameDao.getAllQuestsFlow()

    suspend fun ensureInitialDataLoaded() {
        // Initialize Profile if null
        val existingProfile = gameDao.getPlayerProfileDirect()
        if (existingProfile == null) {
            gameDao.insertOrUpdatePlayerProfile(PlayerProfileEntity())
        }

        // Initialize 9 farm plots (3x3 grid) if empty
        val existingPlots = gameDao.getAllPlotsDirect()
        if (existingPlots.isEmpty()) {
            val initialPlots = (0 until 9).map { index ->
                PlotEntity(
                    plotIndex = index,
                    cropType = if (index == 0) "WHEAT" else "NONE",
                    growthStage = if (index == 0) 3 else 0,
                    isWatered = index == 0,
                    isPlowed = true,
                    growthProgressPct = if (index == 0) 100f else 0f
                )
            }
            gameDao.insertAllPlots(initialPlots)
        }

        // Initialize default inventory items if empty
        val initialSeeds = listOf(
            InventoryEntity("WHEAT", "गेहूं (Wheat)", 20),
            InventoryEntity("FODDER", "हरा चारा (Fodder)", 15),
            InventoryEntity("FISH", "ताजी मछली (Fish)", 2)
        )
        for (item in initialSeeds) {
            if (gameDao.getInventoryItemDirect(item.itemKey) == null) {
                gameDao.insertOrUpdateInventoryItem(item)
            }
        }

        // Initialize Default Initial Livestock (1 Cow, 1 Goat)
        val currentLivestock = gameDao.getAllLivestockFlow().firstOrNull() ?: emptyList()
        if (currentLivestock.isEmpty()) {
            gameDao.insertLivestock(
                LivestockEntity(
                    animalType = "COW",
                    nickname = "गौरी गाय (Gauri)",
                    healthPct = 100,
                    isFedToday = true,
                    milkReadyLiters = 6.0f
                )
            )
            gameDao.insertLivestock(
                LivestockEntity(
                    animalType = "GOAT",
                    nickname = "सोनपरी बकरी (Sonpari)",
                    healthPct = 95,
                    isFedToday = false,
                    milkReadyLiters = 2.0f
                )
            )
        }

        // Initialize NPC Quests
        val initialQuests = listOf(
            QuestEntity(
                questId = "q1_harvest_wheat",
                titleHindi = "रामदीन चाचा की सलाह: गेहूं काटो",
                titleEng = "Chacha's Advice: Harvest Wheat",
                npcName = "रामदीन चाचा (Chacha Ramdin)",
                npcRole = "अनुभवी किसान (Veteran Farmer)",
                descriptionHindi = "अपने खेत में पका हुआ गेहूं काटो और मंडी में बेचकर अनुभव प्राप्त करो।",
                targetType = "HARVEST_CROP",
                targetAmount = 1,
                rewardRupees = 1200,
                rewardExp = 150
            ),
            QuestEntity(
                questId = "q2_milk_cow",
                titleHindi = "राधा डेयरी: शुद्ध दूध का ऑर्डर",
                titleEng = "Radha Dairy: Fresh Milk Order",
                npcName = "राधा (Radha Dairy Wali)",
                npcRole = "डेयरी प्रमुख (Dairy Head)",
                descriptionHindi = "गौशाला में गाय/भैंस को चारा खिलाओ और दूध निकालकर डेयरी में जमा करो।",
                targetType = "MILK_LIVESTOCK",
                targetAmount = 5,
                rewardRupees = 1800,
                rewardExp = 200
            ),
            QuestEntity(
                questId = "q3_buy_tractor",
                titleHindi = "सरपंच जी का मिशन: गाँव में ट्रैक्टर लाओ",
                titleEng = "Sarpanch Mission: Bring a Tractor",
                npcName = "सरपंच हरदेव सिंह (Sarpanch Ji)",
                npcRole = "गाँव के मुखिया (Village Head)",
                descriptionHindi = "वर्कशॉप से स्वराज ट्रैक्टर खरीदो और खेती में क्रांति लाओ!",
                targetType = "BUY_TRACTOR",
                targetAmount = 1,
                rewardRupees = 5000,
                rewardExp = 500
            ),
            QuestEntity(
                questId = "q4_catch_fish",
                titleHindi = "तालाब में मछली पकड़ो",
                titleEng = "Pond Fishing Challenge",
                npcName = "दीनू मछुआरा (Dinu Fisherman)",
                npcRole = "गाँव का मछुआरा (Village Fisherman)",
                descriptionHindi = "गांव के तालाब से 3 ताजी मछलियां पकड़कर मंडी में बेचो।",
                targetType = "CATCH_FISH",
                targetAmount = 3,
                rewardRupees = 2500,
                rewardExp = 300
            )
        )
        gameDao.insertAllQuests(initialQuests)
    }

    // --- FARMING & PLOT ACTIONS ---

    suspend fun plowPlot(plot: PlotEntity) {
        val profile = gameDao.getPlayerProfileDirect() ?: return
        // Check tractor boost
        val mult = when (profile.activeTractor) {
            "SWARAJ_744" -> 2.5f
            "MAHINDRA_575" -> 4.0f
            "COMBINE_PRO" -> 6.0f
            else -> 1.0f
        }
        val updated = plot.copy(
            isPlowed = true,
            growthStage = 0
        )
        gameDao.updatePlot(updated)
    }

    suspend fun plantCrop(plot: PlotEntity, cropType: CropType): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        if (profile.rupees < cropType.seedPrice) return false

        // Deduct seed cost & plant
        val newProfile = profile.copy(rupees = profile.rupees - cropType.seedPrice)
        gameDao.insertOrUpdatePlayerProfile(newProfile)

        val updatedPlot = plot.copy(
            cropType = cropType.key,
            growthStage = 1, // Seeded
            plantedTimeMs = System.currentTimeMillis(),
            growthProgressPct = 10f,
            isWatered = profile.currentWeather == "RAINY"
        )
        gameDao.updatePlot(updatedPlot)
        return true
    }

    suspend fun waterPlot(plot: PlotEntity) {
        val updated = plot.copy(
            isWatered = true,
            growthProgressPct = (plot.growthProgressPct + 25f).coerceAtMost(100f)
        )
        gameDao.updatePlot(updated)
    }

    suspend fun fertilizePlot(plot: PlotEntity) {
        val profile = gameDao.getPlayerProfileDirect() ?: return
        if (profile.rupees < 50) return
        val newProfile = profile.copy(rupees = profile.rupees - 50)
        gameDao.insertOrUpdatePlayerProfile(newProfile)

        val updated = plot.copy(
            isFertilized = true,
            growthProgressPct = (plot.growthProgressPct + 35f).coerceAtMost(100f)
        )
        gameDao.updatePlot(updated)
    }

    suspend fun harvestPlot(plot: PlotEntity): Int {
        val crop = CropType.values().find { it.key == plot.cropType } ?: return 0
        val profile = gameDao.getPlayerProfileDirect() ?: return 0

        // Calculate yield
        val isTractorUsed = profile.activeTractor != "DESI_HAL"
        val yieldBonus = if (isTractorUsed) 1.25f else 1.0f
        val totalYieldKg = (crop.yieldKgPerPlot * yieldBonus).toInt()

        // Add to inventory
        val currentInv = gameDao.getInventoryItemDirect(crop.key)?.quantity ?: 0
        gameDao.insertOrUpdateInventoryItem(
            InventoryEntity(crop.key, crop.nameHindi, currentInv + totalYieldKg)
        )

        // Reset plot
        val resetPlot = plot.copy(
            cropType = "NONE",
            growthStage = 0,
            isWatered = false,
            isFertilized = false,
            isPlowed = false,
            growthProgressPct = 0f
        )
        gameDao.updatePlot(resetPlot)

        // Reward EXP & check Quests
        addPlayerExp(50)
        incrementQuestProgress("HARVEST_CROP", 1)

        return totalYieldKg
    }

    suspend fun advanceTimeTick() {
        val plots = gameDao.getAllPlotsDirect()
        val profile = gameDao.getPlayerProfileDirect() ?: return

        for (plot in plots) {
            if (plot.cropType != "NONE" && plot.growthStage in 1..2) {
                val crop = CropType.values().find { it.key == plot.cropType }
                val seasonMatch = crop?.preferredSeason == profile.currentSeason || crop?.preferredSeason == "ALL"
                val seasonSpeedBonus = if (seasonMatch) 1.5f else 1.0f
                val waterBonus = if (plot.isWatered) 1.5f else 0.8f
                val fertilizerBonus = if (plot.isFertilized) 1.3f else 1.0f

                val progressIncrement = 12f * seasonSpeedBonus * waterBonus * fertilizerBonus
                val newProgress = plot.growthProgressPct + progressIncrement

                val newStage = when {
                    newProgress >= 100f -> 3 // Ready
                    newProgress >= 40f -> 2 // Growing
                    else -> 1 // Seeded
                }

                gameDao.updatePlot(
                    plot.copy(
                        growthProgressPct = newProgress.coerceAtMost(100f),
                        growthStage = newStage
                    )
                )
            }
        }
    }

    // --- DAIRY & LIVESTOCK ACTIONS ---

    suspend fun feedLivestock(animal: LivestockEntity): Boolean {
        val fodder = gameDao.getInventoryItemDirect("FODDER")
        if (fodder == null || fodder.quantity <= 0) return false

        // Deduct 1 fodder
        gameDao.insertOrUpdateInventoryItem(fodder.copy(quantity = fodder.quantity - 1))

        val def = AnimalType.values().find { it.key == animal.animalType }
        val addedMilk = (def?.dailyMilkLiters ?: 5.0f)

        val updated = animal.copy(
            isFedToday = true,
            healthPct = (animal.healthPct + 10).coerceAtMost(100),
            milkReadyLiters = animal.milkReadyLiters + addedMilk
        )
        gameDao.updateLivestock(updated)
        return true
    }

    suspend fun collectMilk(animal: LivestockEntity): Float {
        val liters = animal.milkReadyLiters
        if (liters <= 0f) return 0f

        val currentMilkInInv = gameDao.getInventoryItemDirect("MILK")?.quantity ?: 0
        gameDao.insertOrUpdateInventoryItem(
            InventoryEntity("MILK", "शुद्ध ताज़ा दूध (Liters)", currentMilkInInv + liters.toInt())
        )

        val updated = animal.copy(milkReadyLiters = 0f)
        gameDao.updateLivestock(updated)

        addPlayerExp(30)
        incrementQuestProgress("MILK_LIVESTOCK", liters.toInt())
        return liters
    }

    suspend fun buyAnimal(type: AnimalType, name: String): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        if (profile.rupees < type.purchaseCost) return false

        val newProfile = profile.copy(rupees = profile.rupees - type.purchaseCost)
        gameDao.insertOrUpdatePlayerProfile(newProfile)

        val newAnimal = LivestockEntity(
            animalType = type.key,
            nickname = name.ifBlank { type.nameHindi },
            healthPct = 100,
            isFedToday = true,
            milkReadyLiters = type.dailyMilkLiters
        )
        gameDao.insertLivestock(newAnimal)
        addPlayerExp(100)
        return true
    }

    // --- MANDI MARKETPLACE ---

    suspend fun sellItemInMandi(itemKey: String, qtyToSell: Int, ratePerUnit: Long): Long {
        val inv = gameDao.getInventoryItemDirect(itemKey) ?: return 0L
        val available = inv.quantity
        val actualSell = qtyToSell.coerceAtMost(available)
        if (actualSell <= 0) return 0L

        val earnings = actualSell * ratePerUnit

        // Update Inventory
        gameDao.insertOrUpdateInventoryItem(inv.copy(quantity = available - actualSell))

        // Add Rupees
        val profile = gameDao.getPlayerProfileDirect() ?: return earnings
        val newProfile = profile.copy(rupees = profile.rupees + earnings)
        gameDao.insertOrUpdatePlayerProfile(newProfile)

        addPlayerExp((earnings / 100).toInt().coerceAtLeast(10))
        return earnings
    }

    suspend fun buyFodderOrSeeds(itemKey: String, nameHindi: String, qty: Int, costPerUnit: Long): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        val totalCost = qty * costPerUnit
        if (profile.rupees < totalCost) return false

        // Deduct rupees
        gameDao.insertOrUpdatePlayerProfile(profile.copy(rupees = profile.rupees - totalCost))

        // Add to inventory
        val inv = gameDao.getInventoryItemDirect(itemKey)
        val curQty = inv?.quantity ?: 0
        gameDao.insertOrUpdateInventoryItem(
            InventoryEntity(itemKey, nameHindi, curQty + qty)
        )
        return true
    }

    // --- FISHING ---

    suspend fun catchFish(): FishType {
        val fishList = FishType.values()
        val caught = fishList[Random.nextInt(fishList.size)]

        val inv = gameDao.getInventoryItemDirect("FISH")
        val currentQty = inv?.quantity ?: 0
        gameDao.insertOrUpdateInventoryItem(
            InventoryEntity("FISH", "ताजी मछली (Fish)", currentQty + 1)
        )

        addPlayerExp(25)
        incrementQuestProgress("CATCH_FISH", 1)
        return caught
    }

    // --- HOUSE & MACHINE UPGRADES ---

    suspend fun upgradeHouse(): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        val nextLevel = profile.houseLevel + 1
        val tier = HouseTier.values().find { it.level == nextLevel } ?: return false

        if (profile.rupees < tier.upgradeCost) return false

        val updatedProfile = profile.copy(
            houseLevel = nextLevel,
            rupees = profile.rupees - tier.upgradeCost,
            storageCapacityKg = tier.storageCapacity
        )
        gameDao.insertOrUpdatePlayerProfile(updatedProfile)
        addPlayerExp(300)
        return true
    }

    suspend fun buyTubewellOrSolar(isSolar: Boolean): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        val cost = if (isSolar) 40000L else 20000L
        if (profile.rupees < cost) return false

        val updated = if (isSolar) {
            profile.copy(hasSolarPump = true, rupees = profile.rupees - cost)
        } else {
            profile.copy(hasTubewell = true, rupees = profile.rupees - cost)
        }
        gameDao.insertOrUpdatePlayerProfile(updated)
        addPlayerExp(200)
        return true
    }

    suspend fun buyMachinery(machinery: MachineryType): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        if (profile.rupees < machinery.cost) return false

        val updated = profile.copy(
            rupees = profile.rupees - machinery.cost,
            activeTractor = machinery.key
        )
        gameDao.insertOrUpdatePlayerProfile(updated)

        addPlayerExp(500)
        incrementQuestProgress("BUY_TRACTOR", 1)
        return true
    }

    suspend fun buyDiesel(liters: Float): Boolean {
        val profile = gameDao.getPlayerProfileDirect() ?: return false
        val cost = (liters * 90).toLong() // ₹90 per liter
        if (profile.rupees < cost) return false

        val updated = profile.copy(
            rupees = profile.rupees - cost,
            dieselLiters = (profile.dieselLiters + liters).coerceAtMost(100f)
        )
        gameDao.insertOrUpdatePlayerProfile(updated)
        return true
    }

    // --- DAY & WEATHER ENGINE ---

    suspend fun advanceDay() {
        val profile = gameDao.getPlayerProfileDirect() ?: return

        val nextDay = profile.currentDay + 1

        // Rotate season every 5 days
        val seasons = SeasonType.values()
        val seasonIndex = ((nextDay - 1) / 5) % seasons.size
        val newSeason = seasons[seasonIndex].key

        // Random Weather
        val weathers = listOf("SUNNY", "RAINY", "CLOUDY", "HEATWAVE")
        val newWeather = weathers[Random.nextInt(weathers.size)]

        val updated = profile.copy(
            currentDay = nextDay,
            currentSeason = newSeason,
            currentWeather = newWeather
        )
        gameDao.insertOrUpdatePlayerProfile(updated)

        // Automatically grow crops on new day
        advanceTimeTick()
    }

    // --- QUEST ENGINE ---

    private suspend fun incrementQuestProgress(targetType: String, amount: Int) {
        val quests = gameDao.getAllQuestsFlow().firstOrNull() ?: return
        for (q in quests) {
            if (q.targetType == targetType && !q.isCompleted) {
                val newAmount = q.currentAmount + amount
                val completed = newAmount >= q.targetAmount
                val updated = q.copy(
                    currentAmount = newAmount.coerceAtMost(q.targetAmount),
                    isCompleted = completed
                )
                gameDao.updateQuest(updated)
            }
        }
    }

    suspend fun claimQuestReward(quest: QuestEntity) {
        if (!quest.isCompleted || quest.isClaimed) return

        val profile = gameDao.getPlayerProfileDirect() ?: return
        val updatedProfile = profile.copy(
            rupees = profile.rupees + quest.rewardRupees
        )
        gameDao.insertOrUpdatePlayerProfile(updatedProfile)

        addPlayerExp(quest.rewardExp)
        gameDao.updateQuest(quest.copy(isClaimed = true))
    }

    private suspend fun addPlayerExp(amount: Int) {
        val profile = gameDao.getPlayerProfileDirect() ?: return
        var newExp = profile.exp + amount
        var newLevel = profile.level
        val expForNextLevel = newLevel * 200

        if (newExp >= expForNextLevel) {
            newLevel += 1
            newExp -= expForNextLevel
        }

        gameDao.insertOrUpdatePlayerProfile(
            profile.copy(
                exp = newExp,
                level = newLevel
            )
        )
    }
}

enum class FishType(
    val nameHindi: String,
    val nameEng: String,
    val weightKg: Float,
    val valueRupees: Long,
    val iconEmoji: String
) {
    KATLA("कटला मछली (Katla)", "Katla", 2.5f, 350, "🐟"),
    ROHU("रोहू मछली (Rohu)", "Rohu", 1.8f, 280, "🐠"),
    SINGHI("सिंगी देशी मछली (Singhi)", "Singhi", 0.9f, 400, "🎣")
}
