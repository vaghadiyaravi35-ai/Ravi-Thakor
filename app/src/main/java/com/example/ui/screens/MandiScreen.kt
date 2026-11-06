package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.data.model.CropType
import com.example.data.model.InventoryEntity

data class MandiCommodity(
    val key: String,
    val nameHindi: String,
    val ratePerUnit: Long,
    val unitLabel: String,
    val trendUp: Boolean,
    val iconEmoji: String
)

@Composable
fun MandiScreen(
    inventory: List<InventoryEntity>,
    onSellItem: (itemKey: String, qty: Int, rate: Long) -> Unit,
    onBuyItem: (itemKey: String, nameHindi: String, qty: Int, costPerUnit: Long) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Sell Crops & Goods, 1 = Buy Seeds & Fodder

    val commodities = listOf(
        MandiCommodity("WHEAT", "गेहूं (Wheat)", 32, "Kg", true, "🌾"),
        MandiCommodity("BAJRA", "बाजरा (Bajra)", 26, "Kg", true, "🌽"),
        MandiCommodity("COTTON", "कपास (Cotton)", 88, "Kg", false, "☁️"),
        MandiCommodity("PADDY", "धान / चावल (Paddy)", 44, "Kg", true, "🌱"),
        MandiCommodity("VEGETABLES", "ताज़ी सब्जियां", 38, "Kg", true, "🥦"),
        MandiCommodity("MILK", "शुद्ध ताज़ा दूध", 65, "Liter", true, "🥛"),
        MandiCommodity("FISH", "ताजी तालाब मछली", 320, "Kg", true, "🐟")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Banner
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "🛒 किसान मंडी बाज़ार (Mandi Market)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "आज का बाज़ार भाव और सीधे बिक्री",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Text(text = "📈 भाव चढ़े हैं", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
        }

        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .testTag("mandi_tabs")
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("फसल व दूध बेचें 💰", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("बीज व चारा खरीदें 📦", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
        }

        if (selectedTab == 0) {
            // Sell Inventory View
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(commodities) { comm ->
                    val invItem = inventory.find { it.itemKey == comm.key }
                    val currentQty = invItem?.quantity ?: 0

                    MandiSellCard(
                        commodity = comm,
                        quantityAvailable = currentQty,
                        onSell = { qty -> onSellItem(comm.key, qty, comm.ratePerUnit) }
                    )
                }
            }
        } else {
            // Buy Supplies View
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    MandiBuyCard(
                        title = "हरा पशु चारा (Green Fodder)",
                        emoji = "🌿",
                        subtitle = "गौशाला में गाय/भैंस का भोजन",
                        pricePerUnit = 20,
                        unit = "बंडल",
                        onBuy = { qty -> onBuyItem("FODDER", "हरा चारा", qty, 20) }
                    )
                }
                for (crop in CropType.values()) {
                    item {
                        MandiBuyCard(
                            title = "${crop.nameHindi} के बीज",
                            emoji = crop.iconEmoji,
                            subtitle = "उपज: ${crop.yieldKgPerPlot}Kg प्रति खेत",
                            pricePerUnit = crop.seedPrice,
                            unit = "पैकेट",
                            onBuy = { qty -> onBuyItem("SEED_${crop.key}", "${crop.nameHindi} बीज", qty, crop.seedPrice) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MandiSellCard(
    commodity: MandiCommodity,
    quantityAvailable: Int,
    onSell: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = commodity.iconEmoji, fontSize = 28.sp)
                Column {
                    Text(
                        text = commodity.nameHindi,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "भाव: ₹${commodity.ratePerUnit}/${commodity.unitLabel} | स्टॉक: $quantityAvailable ${commodity.unitLabel}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = { onSell(10) },
                    enabled = quantityAvailable >= 10,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("10 बेचें", fontSize = 10.sp)
                }

                Button(
                    onClick = { onSell(quantityAvailable) },
                    enabled = quantityAvailable > 0,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("सब बेचें 💰", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun MandiBuyCard(
    title: String,
    emoji: String,
    subtitle: String,
    pricePerUnit: Long,
    unit: String,
    onBuy: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = emoji, fontSize = 28.sp)
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$subtitle | मूल्य: ₹$pricePerUnit/$unit",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = { onBuy(5) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("5 लें (₹${pricePerUnit * 5})", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
