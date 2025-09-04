package de.schnitzel.nutrition.database.tables

import org.jetbrains.exposed.sql.Table

object PlayerNutritionEntryTable : Table("player_nutrition_entries") {
    val playerUuid = uuid("player_uuid").uniqueIndex()
    val nuScore = integer("nuScore").default(20)
    val fruit = float("fruit").default(5.0f)
    val sugar = float("sugar").default(5.0f)
    val cereals = float("cereals").default(5.0F)

    override val primaryKey = PrimaryKey(playerUuid)
}