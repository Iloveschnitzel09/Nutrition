package de.schnitzel.nutrition.util

import java.util.*

data class NutritionData(
    val playerUuid: UUID,
    var nuScore: Int,
    var fruit: Int,
    var sugar: Int,
    var cereals: Int,
    var meat: Int,
    var dairy: Int
)