package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MelaFestivalScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF8E24AA)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎪 गाँव का मेला व सांस्कृतिक महोत्सव (Village Mela)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "बैसाखी, दीवाली व मकर संक्रांति के उपलक्ष्य में रंगारंग मेला!",
                    fontSize = 12.sp,
                    color = Color(0xFFE1BEE7)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                MelaStallCard(
                    title = "🍩 रामू काका का जलेबी व गुड़ स्टॉल",
                    subtitle = "ताज़ा देसी घी की जलेबी और नए गुड़ का स्वाद",
                    rewardText = "+100 EXP ऊर्जा प्रोत्साहन",
                    color = Color(0xFFFF6F00)
                )
            }
            item {
                MelaStallCard(
                    title = "🎯 निशानेबाज़ी व कठपुतली शो",
                    subtitle = "पारंपरिक राजस्थानी कठपुतली एवं गुलेल प्रतियोगिता",
                    rewardText = "लकी ड्रॉ में ₹500 पुरस्कार",
                    color = Color(0xFF00838F)
                )
            }
            item {
                MelaStallCard(
                    title = "🏛️ ग्राम पंचायत चौपाल (Panchayat Ground)",
                    subtitle = "सरपंच हरदेव सिंह द्वारा उत्तम किसान सम्मान समारोह",
                    rewardText = "उत्कृष्ट किसान ट्रॉफी 🏆",
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}

@Composable
fun MelaStallCard(
    title: String,
    subtitle: String,
    rewardText: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color
            ) {
                Text(
                    text = rewardText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
