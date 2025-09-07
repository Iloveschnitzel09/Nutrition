package de.schnitzel.nutrition.database.tables

import org.jetbrains.exposed.sql.Table

object PlayerNutritionEntryTable : Table("player_nutrition_entries") {
    val playerUuid = uuid("player_uuid").uniqueIndex()
    val nuScore = integer("nuScore").default(20)
    val fruit = integer("fruit").default(10)
    val sugar = integer("sugar").default(10)
    val cereals = integer("cereals").default(10)
    val meat = integer("meat").default(10)
    val dairy = integer("dairy").default(10)

    override val primaryKey = PrimaryKey(playerUuid)
}