package de.schnitzel.nutrition

import java.util.UUID

data class NutritionData (
    val playerUuid: UUID,
    val nuScore: Int,
    val fruit: Float,
    val sugar: Float,
    val cereals: Float,
    val meat: Float,
    val dairy: Float
)