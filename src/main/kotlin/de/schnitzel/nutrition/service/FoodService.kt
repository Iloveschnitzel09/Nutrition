package de.schnitzel.nutrition.service

import de.schnitzel.nutrition.plugin
import de.schnitzel.nutrition.util.FoodCategory
import de.schnitzel.nutrition.util.NutritionData

/*
 * Danke ChatGPT für die Config-Ladehilfe :)
 */

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object FoodService {

    val edibleItems: MutableMap<Material, Map<FoodCategory, Int>> = mutableMapOf()

    fun load() {
        edibleItems.clear()

        val section = plugin.config.getConfigurationSection("edible-items") ?: return
        for (itemName in section.getKeys(false)) {
            val material = Material.matchMaterial(itemName)

            if (material == null) {
                plugin.logger.warning("Ungültiges Item in config.yml: $itemName")
                continue
            }

            val categorySection = section.getConfigurationSection(itemName)
            if (categorySection == null) {
                plugin.logger.warning("Keine Kategorien für Item '$itemName' gefunden")
                continue
            }

            val categoryMap = mutableMapOf<FoodCategory, Int>()
            for (categoryName in categorySection.getKeys(false)) {
                try {
                    val category = FoodCategory.valueOf(categoryName.uppercase())
                    val value = categorySection.getInt(categoryName, 0) // Default = 0
                    categoryMap[category] = value
                } catch (e: IllegalArgumentException) {
                    plugin.logger.warning("Ungültige Kategorie '$categoryName' für Item '$itemName'")
                }
            }

            edibleItems[material] = categoryMap
        }
    }

    fun getNutritionDataForItem(player: Player, item: Material): NutritionData {
        val categories = edibleItems[item] ?: return NutritionData(
            playerUuid = player.uniqueId,
            nuScore = 0,
            fruit = 0,
            sugar = 0,
            cereals = 0,
            meat = 0,
            dairy = 0
        )

        return NutritionData(
            playerUuid = player.uniqueId,
            nuScore = 0, // kannst du später berechnen, z. B. Summe oder Formel
            fruit = categories[FoodCategory.FRUECHTE] ?: 0,
            sugar = categories[FoodCategory.ZUCKER] ?: 0,
            cereals = categories[FoodCategory.GETREIDE] ?: 0,
            meat = categories[FoodCategory.FLEISCH] ?: 0,
            dairy = categories[FoodCategory.MILCH] ?: 0
        )
    }
}