package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.*
import com.example.data.repository.FishType
import com.example.data.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository

    val playerProfile: StateFlow<PlayerProfileEntity?>
    val farmPlots: StateFlow<List<PlotEntity>>
    val livestockList: StateFlow<List<LivestockEntity>>
    val inventoryList: StateFlow<List<InventoryEntity>>
    val questsList: StateFlow<List<QuestEntity>>

    // UI state toast/snack notifications
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Active Navigation Screen
    private val _currentTab = MutableStateFlow("MAP") // MAP, FARM, DAIRY, MANDI, HOUSE, WORKSHOP, POND, MELA, QUESTS
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = GameRepository(database.gameDao())

        viewModelScope.launch {
            repository.ensureInitialDataLoaded()
        }

        playerProfile = repository.playerProfileFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        farmPlots = repository.allPlotsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        livestockList = repository.allLivestockFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        inventoryList = repository.allInventoryFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        questsList = repository.allQuestsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Game loop ticker: ticks every 3 seconds to update crop growth & simulation
        viewModelScope.launch {
            while (true) {
                delay(3000)
                repository.advanceTimeTick()
            }
        }
    }

    fun navigateTo(tab: String) {
        _currentTab.value = tab
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    private fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    // --- FARM ACTIONS ---

    fun plowPlot(plot: PlotEntity) {
        viewModelScope.launch {
            repository.plowPlot(plot)
            showMessage("खेत जोता गया! (Plot Plowed)")
        }
    }

    fun plantCrop(plot: PlotEntity, cropType: CropType) {
        viewModelScope.launch {
            val success = repository.plantCrop(plot, cropType)
            if (success) {
                showMessage("${cropType.nameHindi} के बीज बो दिए गए! 🌱")
            } else {
                showMessage("पर्याप्त पैसे नहीं हैं! (Insufficient Rupees)")
            }
        }
    }

    fun waterPlot(plot: PlotEntity) {
        viewModelScope.launch {
            repository.waterPlot(plot)
            showMessage("खेत में पानी लगाया गया! 💧")
        }
    }

    fun fertilizePlot(plot: PlotEntity) {
        viewModelScope.launch {
            repository.fertilizePlot(plot)
            showMessage("खेत में देशी खाद / यूरिया छिड़का गया! 🌾")
        }
    }

    fun harvestPlot(plot: PlotEntity) {
        viewModelScope.launch {
            val yieldKg = repository.harvestPlot(plot)
            if (yieldKg > 0) {
                showMessage("फसल की कटाई सफल! $yieldKg किलो भंडार में जुड़ा। 🎉")
            }
        }
    }

    // --- DAIRY ACTIONS ---

    fun feedLivestock(animal: LivestockEntity) {
        viewModelScope.launch {
            val success = repository.feedLivestock(animal)
            if (success) {
                showMessage("${animal.nickname} को हरा चारा खिलाया गया! 🌿")
            } else {
                showMessage("भंडार में हरा चारा उपलब्ध नहीं है! मंडी से खरीदें।")
            }
        }
    }

    fun collectMilk(animal: LivestockEntity) {
        viewModelScope.launch {
            val liters = repository.collectMilk(animal)
            if (liters > 0f) {
                showMessage("गौशाला से $liters लीटर दूध निकाला गया! 🥛")
            } else {
                showMessage("दूध तैयार नहीं है! पहले पशु को चारा खिलाएं।")
            }
        }
    }

    fun buyAnimal(type: AnimalType, name: String) {
        viewModelScope.launch {
            val success = repository.buyAnimal(type, name)
            if (success) {
                showMessage("बधाई हो! नया पशु ${type.nameHindi} गौशाला में आ गया। 🐄")
            } else {
                showMessage("पैसे कम हैं! पशु खरीदने के लिए और कमाई करें।")
            }
        }
    }

    // --- MANDI ACTIONS ---

    fun sellItemInMandi(itemKey: String, qty: Int, rate: Long) {
        viewModelScope.launch {
            val earnings = repository.sellItemInMandi(itemKey, qty, rate)
            if (earnings > 0) {
                showMessage("मंडी में बिक्री सफल! ₹$earnings प्राप्त हुए। 🛒")
            } else {
                showMessage("भंडार में पर्याप्त सामग्री नहीं है।")
            }
        }
    }

    fun buyFodderOrSeeds(itemKey: String, nameHindi: String, qty: Int, costPerUnit: Long) {
        viewModelScope.launch {
            val success = repository.buyFodderOrSeeds(itemKey, nameHindi, qty, costPerUnit)
            if (success) {
                showMessage("खरीद सफल! $qty x $nameHindi भंडार में जुड़ा। 📦")
            } else {
                showMessage("पर्याप्त रुपये नहीं हैं! ₹${qty * costPerUnit} चाहिए।")
            }
        }
    }

    // --- FISHING ACTIONS ---

    fun catchFish(onCaught: (FishType) -> Unit) {
        viewModelScope.launch {
            val fish = repository.catchFish()
            onCaught(fish)
            showMessage("शाबाश! आपने ${fish.nameHindi} पकड़ी (मूल्य ₹${fish.valueRupees}) 🎣")
        }
    }

    // --- UPGRADES ---

    fun upgradeHouse() {
        viewModelScope.launch {
            val success = repository.upgradeHouse()
            if (success) {
                showMessage("बधाई हो! आपका घर अपग्रेड हो गया। 🏡")
            } else {
                showMessage("घर अपग्रेड के लिए पर्याप्त पैसे नहीं हैं।")
            }
        }
    }

    fun buyTubewellOrSolar(isSolar: Boolean) {
        viewModelScope.launch {
            val success = repository.buyTubewellOrSolar(isSolar)
            if (success) {
                val item = if (isSolar) "सोलर पंप" else "समरसेबल ट्यूबवेल"
                showMessage("$item खेत में स्थापित हो गया! 💧")
            } else {
                showMessage("पर्याप्त पैसे नहीं हैं।")
            }
        }
    }

    fun buyMachinery(machinery: MachineryType) {
        viewModelScope.launch {
            val success = repository.buyMachinery(machinery)
            if (success) {
                showMessage("बधाई हो! नया ${machinery.nameHindi} वर्कशॉप में आ गया! 🚜")
            } else {
                showMessage("मशीन खरीदने के लिए और पैसे कमाएं।")
            }
        }
    }

    fun buyDiesel(liters: Float) {
        viewModelScope.launch {
            val success = repository.buyDiesel(liters)
            if (success) {
                showMessage("ट्रैक्टर में $liters लीटर डीजल भरा गया! ⛽")
            } else {
                showMessage("डीजल खरीदने के लिए पैसे कम हैं।")
            }
        }
    }

    // --- ADVANCE DAY ---

    fun nextDay() {
        viewModelScope.launch {
            repository.advanceDay()
            showMessage("नया दिन शुरू हुआ! सूरज उगा और फसलें बढ़ीं। 🌅")
        }
    }

    // --- QUESTS ---

    fun claimQuestReward(quest: QuestEntity) {
        viewModelScope.launch {
            repository.claimQuestReward(quest)
            showMessage("मिशन इनाम प्राप्त हुआ: +₹${quest.rewardRupees} तथा +${quest.rewardExp} EXP 🎉")
        }
    }
}
