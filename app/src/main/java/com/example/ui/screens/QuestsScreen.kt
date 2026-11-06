package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.QuestEntity

@Composable
fun QuestsScreen(
    quests: List<QuestEntity>,
    onClaimReward: (QuestEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Header
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .shadow(3.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "👥 गाँव वाले और मिशन (NPC Quests)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "रामदीन चाचा व सरपंच जी के कार्य पूरे करें और इनाम पाएं",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(quests, key = { it.questId }) { quest ->
                QuestCard(quest = quest, onClaim = { onClaimReward(quest) })
            }
        }
    }
}

@Composable
fun QuestCard(
    quest: QuestEntity,
    onClaim: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "👨‍🌾", fontSize = 28.sp)
                    Column {
                        Text(
                            text = quest.npcName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = quest.titleHindi,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (quest.isClaimed) Color.LightGray else if (quest.isCompleted) Color(0xFF2E7D32) else Color(0xFFFFF3CD)
                ) {
                    Text(
                        text = if (quest.isClaimed) "✅ पूरा हुआ" else if (quest.isCompleted) "🎉 इनाम तैयार!" else "प्रगति: ${quest.currentAmount}/${quest.targetAmount}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quest.isCompleted && !quest.isClaimed) Color.White else Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = quest.descriptionHindi,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "इनाम: +₹${quest.rewardRupees} | +${quest.rewardExp} EXP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (quest.isCompleted && !quest.isClaimed) {
                    Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("claim_quest_${quest.questId}")
                    ) {
                        Text("इनाम लें 💰", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}
