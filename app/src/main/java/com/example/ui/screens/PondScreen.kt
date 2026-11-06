package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.repository.FishType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PondScreen(
    onCatchFishClick: ((FishType) -> Unit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isCasting by remember { mutableStateOf(false) }
    var isWaitingForBite by remember { mutableStateOf(false) }
    var biteReady by remember { mutableStateOf(false) }
    var lastCaughtFish by remember { mutableStateOf<FishType?>(null) }

    fun startFishing() {
        if (isCasting || isWaitingForBite) return
        isCasting = true
        biteReady = false
        lastCaughtFish = null

        coroutineScope.launch {
            delay(1000)
            isCasting = false
            isWaitingForBite = true
            delay(2000) // Simulate fish nibbling
            isWaitingForBite = false
            biteReady = true
        }
    }

    fun pullLine() {
        if (!biteReady) return
        biteReady = false
        onCatchFishClick { fish ->
            lastCaughtFish = fish
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF00838F)),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (biteReady) "🐟 मछली कांटा निगल रही है! खींचो!" else if (isWaitingForBite) "🌊 बंसी पानी में है... प्रतीक्षा करें..." else "🏞️ गाँव का स्वच्छ तालाब (Pond)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (biteReady) {
                        Button(
                            onClick = { pullLine() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F)),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(80.dp)
                                .testTag("pull_fish_button")
                        ) {
                            Text("खींचो! 🎣", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                        }
                    } else if (isCasting || isWaitingForBite) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Button(
                            onClick = { startFishing() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("cast_rod_button")
                        ) {
                            Text("बंसी फेंकें (Cast Rod) 🎣", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00838F))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = lastCaughtFish != null) {
            lastCaughtFish?.let { fish ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = fish.iconEmoji, fontSize = 40.sp)
                        Column {
                            Text(
                                text = "बधाई हो! पकड़ में आई: ${fish.nameHindi}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF006064)
                            )
                            Text(
                                text = "वजन: ${fish.weightKg} Kg | मंडी मूल्य: ₹${fish.valueRupees}",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
