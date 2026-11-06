package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HouseTier
import com.example.data.model.PlayerProfileEntity

@Composable
fun HouseScreen(
    profile: PlayerProfileEntity?,
    onUpgradeHouse: () -> Unit,
    onBuyTubewellOrSolar: (isSolar: Boolean) -> Unit
) {
    val currentLevel = profile?.houseLevel ?: 1
    val currentTier = HouseTier.values().find { it.level == currentLevel } ?: HouseTier.KUTCHA_HUT
    val nextTier = HouseTier.values().find { it.level == currentLevel + 1 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Current House Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .shadow(3.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = when (currentLevel) {
                                1 -> "🏚️"
                                2 -> "🏠"
                                3 -> "🏰"
                                else -> "🏡"
                            },
                            fontSize = 40.sp
                        )
                        Column {
                            Text(
                                text = currentTier.nameHindi,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "भंडारण क्षमता: ${currentTier.storageCapacity} Kg",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "लेवल $currentLevel",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = currentTier.description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                if (nextTier != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onUpgradeHouse,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upgrade_house_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            text = "घर अपग्रेड करें: ${nextTier.nameHindi} (₹${nextTier.upgradeCost}) 🔨",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Text(
            text = "💧 गाँव का पानी व बोरवेल सेटअप (Water Subsystem)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "💧", fontSize = 28.sp)
                            Column {
                                Text(
                                    text = "समरसेबल ट्यूबवेल (Submersible Pump)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "खेत में निरंतर पानी की निर्बाध आपूर्ति",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        if (profile?.hasTubewell == true) {
                            Text("✅ स्थापित है", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 12.sp)
                        } else {
                            Button(
                                onClick = { onBuyTubewellOrSolar(false) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("₹20,000", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "☀️", fontSize = 28.sp)
                            Column {
                                Text(
                                    text = "सौर ऊर्जा सोलर पंप (Solar Water Pump)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "बिना बिजली / डीजल के मुफ्त पानी",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        if (profile?.hasSolarPump == true) {
                            Text("✅ स्थापित है", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 12.sp)
                        } else {
                            Button(
                                onClick = { onBuyTubewellOrSolar(true) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("₹40,000", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
