package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.SeasonType

@Composable
fun TopAppBarHeader(
    profile: PlayerProfileEntity?,
    onAdvanceDayClick: () -> Unit,
    onQuestClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Top Row: Player Info, Rupees, Next Day Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player Name & Level Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👨‍🌾",
                            fontSize = 20.sp
                        )
                    }
                    Column {
                        Text(
                            text = profile?.playerName ?: "किसान भैया",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            ) {
                                Text(
                                    text = "स्तर ${profile?.level ?: 1}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "EXP: ${profile?.exp ?: 0}",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Money Rupees Counter
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "💰 ₹${profile?.rupees ?: 0}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = Color(0xFFFFD54F)
                        )
                    }
                }

                // Advance Day Button
                Button(
                    onClick = onAdvanceDayClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("next_day_button")
                ) {
                    Text(
                        text = "अगला दिन 🌅",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Bar: Day, Season & Weather Badge
            val seasonDef = SeasonType.values().find { it.key == profile?.currentSeason }
            val weatherEmoji = when (profile?.currentWeather) {
                "RAINY" -> "🌧️ बारिश"
                "CLOUDY" -> "⛅ बादल"
                "HEATWAVE" -> "🔥 तीव्र धूप / लू"
                else -> "☀️ खिली धूप"
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "🗓️ दिन ${profile?.currentDay ?: 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Text(
                        text = seasonDef?.nameHindi ?: "ऋतु",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFECB3)
                    )
                }

                Text(
                    text = weatherEmoji,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}
