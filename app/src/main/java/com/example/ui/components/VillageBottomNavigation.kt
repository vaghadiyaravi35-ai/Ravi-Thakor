package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp

data class BottomNavItem(
    val id: String,
    val titleHindi: String,
    val iconEmoji: String
)

@Composable
fun VillageBottomNavigation(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("MAP", "नक्शा", "🗺️"),
        BottomNavItem("FARM", "खेत", "🌾"),
        BottomNavItem("DAIRY", "डेयरी", "🐄"),
        BottomNavItem("MANDI", "मंडी", "🛒"),
        BottomNavItem("QUESTS", "मिशन", "📜")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_navigation_bar")
    ) {
        items.forEach { item ->
            val selected = currentTab == item.id
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item.id) },
                icon = {
                    Text(
                        text = item.iconEmoji,
                        fontSize = if (selected) 22.sp else 18.sp
                    )
                },
                label = {
                    Text(
                        text = item.titleHindi,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
