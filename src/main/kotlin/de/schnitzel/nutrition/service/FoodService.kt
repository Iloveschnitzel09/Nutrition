package de.schnitzel.nutrition.service

import de.schnitzel.nutrition.FoodCategory

import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

object
FoodService {

    val edibleItems : MutableMap<Material, List<FoodCategory>> = mutableMapOf()

    fun load(plugin : JavaPlugin) {
        edibleItems.clear()

        val section = plugin.config.getConfigurationSection("edible-items") ?: return
        for(itemName in section.getKeys(false)) {
            val material = Material.matchMaterial(itemName)

            if (material == null) {
                plugin.logger.warning("Ungültiges Item in config.yml: $itemName")
                continue
            }
            val rawCategories = section.getStringList(itemName)

            val categories = rawCategories.mapNotNull {
                try {
                    FoodCategory.valueOf(it.uppercase())
                } catch (e: IllegalArgumentException) {
                    plugin.logger.warning("Ungültige Kategorie '$it' für Item '$itemName' in config.yml")
                    null
                }
            }

            edibleItems[material] = categories
        }

        plugin.logger.info("Geladene essbare Items: $edibleItems.")
    }
}