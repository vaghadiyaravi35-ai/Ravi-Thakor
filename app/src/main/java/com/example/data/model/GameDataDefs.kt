package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class CropType(
    val key: String,
    val nameHindi: String,
    val nameEng: String,
    val seedPrice: Long,
    val baseSellPriceKg: Long,
    val yieldKgPerPlot: Int,
    val growthDurationSeconds: Int,
    val preferredSeason: String,
    val iconEmoji: String,
    val color: Color
) {
    WHEAT(
        key = "WHEAT",
        nameHindi = "गेहूं (Wheat)",
        nameEng = "Wheat",
        seedPrice = 40,
        baseSellPriceKg = 30,
        yieldKgPerPlot = 25,
        growthDurationSeconds = 12,
        preferredSeason = "SARDI_WINTER",
        iconEmoji = "🌾",
        color = Color(0xFFE6C229)
    ),
    BAJRA(
        key = "BAJRA",
        nameHindi = "बाजरा (Pearl Millet)",
        nameEng = "Bajra",
        seedPrice = 25,
        baseSellPriceKg = 24,
        yieldKgPerPlot = 20,
        growthDurationSeconds = 8,
        preferredSeason = "SURAJ_GARMI",
        iconEmoji = "🌽",
        color = Color(0xFFD4A373)
    ),
    COTTON(
        key = "COTTON",
        nameHindi = "कपास (Cotton)",
        nameEng = "Cotton",
        seedPrice = 100,
        baseSellPriceKg = 85,
        yieldKgPerPlot = 18,
        growthDurationSeconds = 20,
        preferredSeason = "SURAJ_GARMI",
        iconEmoji = "☁️",
        color = Color(0xFFF8F9FA)
    ),
    PADDY(
        key = "PADDY",
        nameHindi = "धान / चावल (Paddy Rice)",
        nameEng = "Paddy",
        seedPrice = 60,
        baseSellPriceKg = 42,
        yieldKgPerPlot = 30,
        growthDurationSeconds = 15,
        preferredSeason = "BARISH_MONSOON",
        iconEmoji = "🌱",
        color = Color(0xFF52B788)
    ),
    VEGETABLES(
        key = "VEGETABLES",
        nameHindi = "ताज़ी सब्जियां (Vegetables)",
        nameEng = "Vegetables",
        seedPrice = 30,
        baseSellPriceKg = 35,
        yieldKgPerPlot = 15,
        growthDurationSeconds = 6,
        preferredSeason = "ALL",
        iconEmoji = "🥦",
        color = Color(0xFF38B000)
    )
}

enum class AnimalType(
    val key: String,
    val nameHindi: String,
    val purchaseCost: Long,
    val dailyMilkLiters: Float,
    val milkPricePerLiter: Long,
    val iconEmoji: String
) {
    COW("COW", "गाय (Desi Cow)", 18000, 8.0f, 60, "🐄"),
    BUFFALO("BUFFALO", "भैंस (Murrah Buffalo)", 32000, 14.0f, 75, "🐃"),
    GOAT("GOAT", "बकरी (Desi Goat)", 4500, 3.0f, 90, "🐐")
}

enum class MachineryType(
    val key: String,
    val nameHindi: String,
    val cost: Long,
    val plowingSpeedFactor: Float, // 1x = hand plow, 3x = tractor
    val harvestBonusPct: Int,
    val iconEmoji: String,
    val reqLevel: Int
) {
    DESI_HAL("DESI_HAL", "लकड़ी का हल (Wooden Plow)", 0, 1.0f, 0, "⛏️", 1),
    SWARAJ_TRACTOR("SWARAJ_744", "स्वराज 744 ट्रैक्टर (Swaraj Tractor)", 85000, 2.5f, 15, "🚜", 2),
    MAHINDRA_TRACTOR("MAHINDRA_575", "महिंद्रा 575 DI ट्रैक्टर (Mahindra Tractor)", 140000, 4.0f, 30, "🚜", 3),
    THRESHER("THRESHER_X", "थ्रेशर मशीन (Thresher Machine)", 45000, 1.0f, 25, "⚙️", 2),
    COMBINE_HARVESTER("COMBINE_PRO", "कंबाइन हार्वेस्टर (Combine Harvester)", 250000, 6.0f, 50, "🚜", 4)
}

enum class SeasonType(
    val key: String,
    val nameHindi: String,
    val description: String,
    val bgGradientColor1: Long,
    val bgGradientColor2: Long
) {
    SURAJ_GARMI("SURAJ_GARMI", "गर्मी (Summer / जायद)", "तेज धूप, बाजरा व कपास की खेती के लिए उत्तम!", 0xFFFFF3CD, 0xFFFFE082),
    BARISH_MONSOON("BARISH_MONSOON", "मानसून (Monsoon / खरीफ)", "झमाझम बारिश, धान (चावल) की खेती के लिए वरदान!", 0xFFE0F7FA, 0xFF80DEEA),
    SARDI_WINTER("SARDI_WINTER", "सर्दी (Winter / रबी)", "ठंडी हवाएं, गेहूं व ताजी सब्जियों का मौसम!", 0xFFE8F5E9, 0xFFA5D6A7)
}

enum class HouseTier(
    val level: Int,
    val nameHindi: String,
    val upgradeCost: Long,
    val description: String,
    val storageCapacity: Int
) {
    KUTCHA_HUT(1, "कच्चा छप्पर मकान (Kutcha Hut)", 0, "पारंपरिक कच्चा झोपड़ी घर", 500),
    BRICK_HOUSE(2, "पक्का ईंट का घर (Brick House)", 25000, "मजबूत ईंट का घर और टिन शेड", 1500),
    HAVELI(3, "गाँव की हवेली (Grand Village Haveli)", 100000, "शानदार राजस्थानी हवेली और विशाल आंगन", 5000),
    SMART_FARMHOUSE(4, "आधुनिक स्मार्ट फार्महाउस (Smart Farmhouse)", 300000, "सोलर पैनल, ऑटोमेटेड साइलो और आलीशान फार्महाउस", 15000)
}

data class MandiRate(
    val itemKey: String,
    val nameHindi: String,
    val currentRatePerKgOrLtr: Long,
    val trendUp: Boolean, // true if price rose today
    val changePct: Int,
    val iconEmoji: String
)
