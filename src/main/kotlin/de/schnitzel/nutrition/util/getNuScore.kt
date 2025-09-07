package de.schnitzel.nutrition.util

fun getNuScore(nutritionData: NutritionData): Int {
    val nuScore =
        (nutritionData.meat + nutritionData.dairy + nutritionData.sugar + nutritionData.cereals + nutritionData.fruit) / 5
    return nuScore.toInt()

}
