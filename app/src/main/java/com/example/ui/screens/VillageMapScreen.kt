package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HouseTier
import com.example.data.model.PlayerProfileEntity

data class VillageZone(
    val id: String,
    val titleHindi: String,
    val titleEng: String,
    val emoji: String,
    val subtitle: String,
    val badgeText: String? = null,
    val cardColor1: Long,
    val cardColor2: Long
)

@Composable
fun VillageMapScreen(
    profile: PlayerProfileEntity?,
    onZoneClick: (String) -> Unit
) {
    val zones = listOf(
        VillageZone(
            id = "FARM",
            titleHindi = "🌾 अपना खेत",
            titleEng = "My Crop Fields",
            emoji = "🚜",
            subtitle = "गेहूं, बाजरा, धान, कपास व सब्जियां उगाएं",
            badgeText = "मुख्य कर्म",
            cardColor1 = 0xFF2E7D32,
            cardColor2 = 0xFF1B5E20
        ),
        VillageZone(
            id = "DAIRY",
            titleHindi = "🐄 गौशाला व डेयरी",
            titleEng = "Dairy & Animals",
            emoji = "🥛",
            subtitle = "गाय, भैंस, बकरी पालें और ताज़ा दूध निकालें",
            badgeText = "डेयरी",
            cardColor1 = 0xFF0288D1,
            cardColor2 = 0xFF01579B
        ),
        VillageZone(
            id = "MANDI",
            titleHindi = "🛒 अनाज व सब्जी मंडी",
            titleEng = "Village Mandi Market",
            emoji = "💰",
            subtitle = "फसल, दूध व मछली बेचें, बीज व खाद खरीदें",
            badgeText = "बाजार",
            cardColor1 = 0xFFF57F17,
            cardColor2 = 0xFFE65100
        ),
        VillageZone(
            id = "WORKSHOP",
            titleHindi = "🚜 ट्रैक्टर व मशीनरी",
            titleEng = "Tractor Workshop",
            emoji = "⚙️",
            subtitle = "स्वराज, महिंद्रा ट्रैक्टर, थ्रेशर व डीजल लें",
            badgeText = "वर्कशॉप",
            cardColor1 = 0xFFD84315,
            cardColor2 = 0xFFBF360C
        ),
        VillageZone(
            id = "HOUSE",
            titleHindi = "🏡 अपना घर व हवेली",
            titleEng = "Home & Tubewell",
            emoji = "🏠",
            subtitle = "कच्चा मकान अपग्रेड करें, सोलर पंप लगाएं",
            badgeText = "लेवल ${profile?.houseLevel ?: 1}",
            cardColor1 = 0xFF5D4037,
            cardColor2 = 0xFF3E2723
        ),
        VillageZone(
            id = "POND",
            titleHindi = "🎣 गाँव का तालाब व नदी",
            titleEng = "Village Pond Fishing",
            emoji = "🐟",
            subtitle = "तालाब में बंसी डालकर रोहू व कटला मछली पकड़ें",
            badgeText = "मछली पकड़ें",
            cardColor1 = 0xFF00838F,
            cardColor2 = 0xFF006064
        ),
        VillageZone(
            id = "MELA",
            titleHindi = "🎪 मेला व पंचायत",
            titleEng = "Village Fair & Mela",
            emoji = "🎉",
            subtitle = "सरपंच जी से मिलें, मेले के त्योहार मनाएं",
            badgeText = "त्योहार",
            cardColor1 = 0xFF8E24AA,
            cardColor2 = 0xFF4A148C
        ),
        VillageZone(
            id = "QUESTS",
            titleHindi = "👥 गाँव वाले व मिशन",
            titleEng = "NPC Quests",
            emoji = "📜",
            subtitle = "रामदीन चाचा व सरपंच के मिशन पूरे करें",
            badgeText = "मिशन",
            cardColor1 = 0xFF00897B,
            cardColor2 = 0xFF004D40
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        // Banner Header
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.desi_village_hero_1786122607546),
                    contentDescription = "Desi Village Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "🌾 मेरा खुशहाल गाँव (My Village)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "खेत जोतें, पशु पालें, ट्रैक्टर चलाएं और गाँव को समृद्ध बनाएं!",
                        fontSize = 12.sp,
                        color = Color(0xFFFFE082)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🗺️ गाँव के प्रमुख स्थान (Explore Village Zones)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Grid of Zones
        val gridItems = zones.chunked(2)
        for (row in gridItems) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (zone in row) {
                    Box(modifier = Modifier.weight(1f)) {
                        ZoneCard(zone = zone, onClick = { onZoneClick(zone.id) })
                    }
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ZoneCard(
    zone: VillageZone,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .clickable { onClick() }
            .testTag("zone_${zone.id.lowercase()}")
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(zone.cardColor1), Color(zone.cardColor2))
                    )
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = zone.emoji,
                        fontSize = 28.sp
                    )
                    if (zone.badgeText != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = zone.badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = zone.titleHindi,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = zone.subtitle,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 2,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}
