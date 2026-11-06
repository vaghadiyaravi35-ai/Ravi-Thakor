package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MachineryType
import com.example.data.model.PlayerProfileEntity

@Composable
fun WorkshopScreen(
    profile: PlayerProfileEntity?,
    onBuyMachinery: (MachineryType) -> Unit,
    onBuyDiesel: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Active Machine Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .shadow(3.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🚜", fontSize = 36.sp)
                        Column {
                            Text(
                                text = "ट्रैक्टर वर्कशॉप व सर्विस (Tractor Garage)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "वर्तमान वाहन: ${profile?.activeTractor ?: "लकड़ी का हल"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Diesel tank
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFF3CD)
                    ) {
                        Text(
                            text = "⛽ डीजल: ${profile?.dieselLiters ?: 0f}L",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF856404),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "डीजल भरवाएं (₹90 / लीटर):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { onBuyDiesel(10f) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(30.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("+10L (₹900)", fontSize = 10.sp)
                        }
                        Button(
                            onClick = { onBuyDiesel(25f) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBF360C)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(30.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("+25L (₹2250)", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Text(
            text = "🛠️ नई कृषि मशीनरी खरीदें (Buy Modern Farming Machinery)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(MachineryType.values()) { machine ->
                val isOwned = profile?.activeTractor == machine.key

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOwned) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isOwned) 2.dp else 0.dp,
                            color = if (isOwned) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .shadow(2.dp, RoundedCornerShape(14.dp))
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
                            Text(text = machine.iconEmoji, fontSize = 32.sp)
                            Column {
                                Text(
                                    text = machine.nameHindi,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "जुताई स्पीड: ${machine.plowingSpeedFactor}x | बोनस उपज: +${machine.harvestBonusPct}%",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        if (isOwned) {
                            Text(
                                text = "✅ चालू वाहन",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Button(
                                onClick = { onBuyMachinery(machine) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("buy_machine_${machine.key.lowercase()}")
                            ) {
                                Text("₹${machine.cost}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
