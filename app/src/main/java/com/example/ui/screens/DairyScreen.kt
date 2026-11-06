package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.AnimalType
import com.example.data.model.LivestockEntity

@Composable
fun DairyScreen(
    livestock: List<LivestockEntity>,
    onFeedAnimal: (LivestockEntity) -> Unit,
    onCollectMilk: (LivestockEntity) -> Unit,
    onBuyAnimalClick: (AnimalType, String) -> Unit
) {
    var showBuyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🐄 गाँव की गौशाला व डेयरी (Dairy Shed)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "पशुओं की देखभाल करें, हरा चारा खिलाएं और शुद्ध दूध बेचें",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { showBuyDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("buy_animal_button")
            ) {
                Text("पशु खरीदें ➕", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (livestock.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "गौशाला खाली है! ऊपर बटन दबाकर नया पशु खरीदें। 🐄",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(livestock, key = { it.id }) { animal ->
                    LivestockCard(
                        animal = animal,
                        onFeed = { onFeedAnimal(animal) },
                        onMilk = { onCollectMilk(animal) }
                    )
                }
            }
        }
    }

    if (showBuyDialog) {
        BuyAnimalModal(
            onDismiss = { showBuyDialog = false },
            onConfirmBuy = { type, name ->
                onBuyAnimalClick(type, name)
                showBuyDialog = false
            }
        )
    }
}

@Composable
fun LivestockCard(
    animal: LivestockEntity,
    onFeed: () -> Unit,
    onMilk: () -> Unit
) {
    val animalDef = AnimalType.values().find { it.key == animal.animalType }
    val emoji = animalDef?.iconEmoji ?: "🐄"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = emoji, fontSize = 42.sp)

                Column {
                    Text(
                        text = animal.nickname,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "स्वास्थ्य: ${animal.healthPct}% | ${if (animal.isFedToday) "🌿 पेट भरा है" else "⚠️ भूखा है"}",
                        fontSize = 11.sp,
                        color = if (animal.isFedToday) Color(0xFF2E7D32) else Color(0xFFD84315)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🥛 तैयार दूध: ${animal.milkReadyLiters} Liter",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = onFeed,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("चारा दें 🌿", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onMilk,
                    enabled = animal.milkReadyLiters > 0f,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("दूध निकालें 🥛", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BuyAnimalModal(
    onDismiss: () -> Unit,
    onConfirmBuy: (AnimalType, String) -> Unit
) {
    var selectedType by remember { mutableStateOf(AnimalType.COW) }
    var nicknameInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "🐄 नया दुधारू पशु खरीदें (Buy Livestock)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (type in AnimalType.values()) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedType == type) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (selectedType == type) 2.dp else 0.dp,
                                color = if (selectedType == type) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedType = type }
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
                                Text(text = type.iconEmoji, fontSize = 28.sp)
                                Column {
                                    Text(
                                        text = type.nameHindi,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "दूध: ${type.dailyMilkLiters}L/दिन @ ₹${type.milkPricePerLiter}/L",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Text(
                                text = "₹${type.purchaseCost}",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = nicknameInput,
                    onValueChange = { nicknameInput = it },
                    label = { Text("पशु का नाम (उदा: गौरी, लाली)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmBuy(selectedType, nicknameInput) }
            ) {
                Text("खरीदें (Confirm Purchase)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द करें")
            }
        }
    )
}
