package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CropType
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.PlotEntity

@Composable
fun FarmScreen(
    profile: PlayerProfileEntity?,
    plots: List<PlotEntity>,
    onPlowPlot: (PlotEntity) -> Unit,
    onPlantCrop: (PlotEntity, CropType) -> Unit,
    onWaterPlot: (PlotEntity) -> Unit,
    onFertilizePlot: (PlotEntity) -> Unit,
    onHarvestPlot: (PlotEntity) -> Unit
) {
    var selectedPlotForPlanting by remember { mutableStateOf<PlotEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Equipment Banner
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .shadow(2.dp, RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (profile?.activeTractor != "DESI_HAL") "🚜" else "⛏️",
                        fontSize = 24.sp
                    )
                    Column {
                        Text(
                            text = if (profile?.activeTractor != "DESI_HAL") "सक्रिय ट्रैक्टर: ${profile?.activeTractor}" else "उपकरण: लकड़ी का हल (Desi Hal)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "सिंचाई साधन: ${if (profile?.hasSolarPump == true) "☀️ सोलर पंप" else if (profile?.hasTubewell == true) "💧 ट्यूबवेल" else "🪣 हाथ से पानी"}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Text(
            text = "🌾 मेरा खेत (Farm Grid - 9 Plots)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3x3 Plot Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(plots, key = { it.id }) { plot ->
                PlotCard(
                    plot = plot,
                    onPlow = { onPlowPlot(plot) },
                    onPlantClick = { selectedPlotForPlanting = plot },
                    onWater = { onWaterPlot(plot) },
                    onFertilize = { onFertilizePlot(plot) },
                    onHarvest = { onHarvestPlot(plot) }
                )
            }
        }
    }

    // Seed Picker Modal Dialog
    if (selectedPlotForPlanting != null) {
        SeedPickerDialog(
            onDismiss = { selectedPlotForPlanting = null },
            onSeedSelected = { cropType ->
                selectedPlotForPlanting?.let { onPlantCrop(it, cropType) }
                selectedPlotForPlanting = null
            }
        )
    }
}

@Composable
fun PlotCard(
    plot: PlotEntity,
    onPlow: () -> Unit,
    onPlantClick: () -> Unit,
    onWater: () -> Unit,
    onFertilize: () -> Unit,
    onHarvest: () -> Unit
) {
    val cropDef = CropType.values().find { it.key == plot.cropType }

    val bgColor = when {
        plot.growthStage == 3 -> Color(0xFFFFF9C4) // Mature Golden
        plot.cropType != "NONE" -> Color(0xFFE8F5E9) // Growing Light Green
        plot.isPlowed -> Color(0xFFD7CCC8) // Plowed Soil Brown
        else -> Color(0xFF8D6E63) // Raw Mud Brown
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .border(
                width = if (plot.growthStage == 3) 2.dp else 1.dp,
                color = if (plot.growthStage == 3) Color(0xFFF57F17) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .testTag("plot_${plot.plotIndex}")
            .shadow(2.dp, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Plot Header Index & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${plot.plotIndex + 1}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                if (plot.isWatered) {
                    Text(text = "💧", fontSize = 12.sp)
                }
            }

            // Center Content (Crop icon / Soil state)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (plot.cropType != "NONE" && cropDef != null) {
                    Text(
                        text = if (plot.growthStage == 3) cropDef.iconEmoji else "🌱",
                        fontSize = 32.sp
                    )
                    Text(
                        text = cropDef.nameEng,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else if (plot.isPlowed) {
                    Text(text = "🟫", fontSize = 28.sp)
                    Text(
                        text = "जोता हुआ",
                        fontSize = 11.sp,
                        color = Color(0xFF4E342E)
                    )
                } else {
                    Text(text = "🧱", fontSize = 28.sp)
                    Text(
                        text = "कच्ची ज़मीन",
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }

            // Action Buttons based on Growth State
            when {
                !plot.isPlowed -> {
                    Button(
                        onClick = onPlow,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037)),
                        contentPadding = PaddingValues(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Text("हल चलाएं ⛏️", fontSize = 10.sp, color = Color.White)
                    }
                }
                plot.cropType == "NONE" -> {
                    Button(
                        onClick = onPlantClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        contentPadding = PaddingValues(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Text("बीज बोएं 🌱", fontSize = 10.sp, color = Color.White)
                    }
                }
                plot.growthStage == 3 -> {
                    Button(
                        onClick = onHarvest,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57F17)),
                        contentPadding = PaddingValues(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    ) {
                        Text("काटें 🌾", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
                else -> {
                    // Growing State - Progress Bar & Water/Fertilizer Row
                    Column(modifier = Modifier.fillMaxWidth()) {
                        LinearProgressIndicator(
                            progress = { plot.growthProgressPct / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = Color(0xFF2E7D32),
                            trackColor = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = onWater,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(26.dp)
                                    .background(Color(0xFF0288D1), RoundedCornerShape(6.dp))
                            ) {
                                Text("💧", fontSize = 10.sp)
                            }
                            IconButton(
                                onClick = onFertilize,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(26.dp)
                                    .background(Color(0xFF388E3C), RoundedCornerShape(6.dp))
                            ) {
                                Text("🌾", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeedPickerDialog(
    onDismiss: () -> Unit,
    onSeedSelected: (CropType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "🌱 फसल का बीज चुनें (Choose Crop Seed)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (crop in CropType.values()) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSeedSelected(crop) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = crop.iconEmoji, fontSize = 24.sp)
                                Column {
                                    Text(
                                        text = crop.nameHindi,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "उपज: ${crop.yieldKgPerPlot}Kg | समय: ${crop.growthDurationSeconds}s",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Text(
                                text = "₹${crop.seedPrice}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द करें (Cancel)")
            }
        }
    )
}
