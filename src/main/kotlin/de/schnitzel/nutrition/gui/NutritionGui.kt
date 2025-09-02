package de.schnitzel.nutrition.gui

import de.schnitzel.nutrition.service.FoodService
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


object NutritionGui {

    fun tryOpen(player: Player, material: Material) {


        val categories = FoodService.edibleItems[material]

        if (categories == null) {
            player.sendMessage("Unbekanntes oder nicht kategorisiertes Nahrungsmittel: ${material.name}")
            return
        }


    }
}