package de.schnitzel.nutrition.database.tables

import org.jetbrains.exposed.sql.Table

object PlayerNutritionEntryTable : Table("player_nutrition_entries") {//TODO: use IntIdTable
    val playerUuid = uuid("player_uuid").uniqueIndex()
    val nuScore = integer("nuScore").default(10)
    val fruit = integer("fruit").default(0)
    val sugar = integer("sugar").default(0)
    val cereals = integer("cereals").default(0)
    val meat = integer("meat").default(0)
    val dairy = integer("dairy").default(0)

    override val primaryKey = PrimaryKey(playerUuid)//TODO: remove when using new table type
}