package de.schnitzel.nutrition.database.tables

import org.jetbrains.exposed.sql.Table

object PlayerNutritionEntryTable : Table("player_nutrition_entries") {
    val playerUuid = uuid("player_uuid").uniqueIndex()
    val nutrition = integer("nutrition").default(20)
    val saturation = float("saturation").default(5.0f)

    override val primaryKey = PrimaryKey(playerUuid)
}