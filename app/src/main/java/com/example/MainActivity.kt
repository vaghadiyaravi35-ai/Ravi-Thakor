package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TopAppBarHeader
import com.example.ui.components.VillageBottomNavigation
import com.example.ui.screens.*
import com.example.ui.theme.KhetiBaadiTheme
import com.example.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KhetiBaadiTheme {
                val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
                val plots by viewModel.farmPlots.collectAsStateWithLifecycle()
                val livestock by viewModel.livestockList.collectAsStateWithLifecycle()
                val inventory by viewModel.inventoryList.collectAsStateWithLifecycle()
                val quests by viewModel.questsList.collectAsStateWithLifecycle()
                val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
                val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(userMessage) {
                    userMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearUserMessage()
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBarHeader(
                            profile = profile,
                            onAdvanceDayClick = { viewModel.nextDay() },
                            onQuestClick = { viewModel.navigateTo("QUESTS") }
                        )
                    },
                    bottomBar = {
                        VillageBottomNavigation(
                            currentTab = currentTab,
                            onTabSelected = { tab -> viewModel.navigateTo(tab) }
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            "MAP" -> VillageMapScreen(
                                profile = profile,
                                onZoneClick = { zoneId -> viewModel.navigateTo(zoneId) }
                            )
                            "FARM" -> FarmScreen(
                                profile = profile,
                                plots = plots,
                                onPlowPlot = { plot -> viewModel.plowPlot(plot) },
                                onPlantCrop = { plot, crop -> viewModel.plantCrop(plot, crop) },
                                onWaterPlot = { plot -> viewModel.waterPlot(plot) },
                                onFertilizePlot = { plot -> viewModel.fertilizePlot(plot) },
                                onHarvestPlot = { plot -> viewModel.harvestPlot(plot) }
                            )
                            "DAIRY" -> DairyScreen(
                                livestock = livestock,
                                onFeedAnimal = { animal -> viewModel.feedLivestock(animal) },
                                onCollectMilk = { animal -> viewModel.collectMilk(animal) },
                                onBuyAnimalClick = { type, name -> viewModel.buyAnimal(type, name) }
                            )
                            "MANDI" -> MandiScreen(
                                inventory = inventory,
                                onSellItem = { itemKey, qty, rate -> viewModel.sellItemInMandi(itemKey, qty, rate) },
                                onBuyItem = { itemKey, name, qty, cost -> viewModel.buyFodderOrSeeds(itemKey, name, qty, cost) }
                            )
                            "WORKSHOP" -> WorkshopScreen(
                                profile = profile,
                                onBuyMachinery = { machine -> viewModel.buyMachinery(machine) },
                                onBuyDiesel = { liters -> viewModel.buyDiesel(liters) }
                            )
                            "HOUSE" -> HouseScreen(
                                profile = profile,
                                onUpgradeHouse = { viewModel.upgradeHouse() },
                                onBuyTubewellOrSolar = { isSolar -> viewModel.buyTubewellOrSolar(isSolar) }
                            )
                            "POND" -> PondScreen(
                                onCatchFishClick = { callback -> viewModel.catchFish(callback) }
                            )
                            "MELA" -> MelaFestivalScreen()
                            "QUESTS" -> QuestsScreen(
                                quests = quests,
                                onClaimReward = { quest -> viewModel.claimQuestReward(quest) }
                            )
                        }
                    }
                }
            }
        }
    }
}
