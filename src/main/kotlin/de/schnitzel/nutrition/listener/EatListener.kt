package de.schnitzel.nutrition.listener

import de.schnitzel.nutrition.service.FoodService
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

class EatListener : Listener {

    @EventHandler
    fun onEat(event: PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item.type

        val categories = FoodService.edibleItems[item]

        if (categories.isNullOrEmpty()) {
            player.sendMessage("Unbekanntes oder nicht kategorisiertes Nahrungsmittel: ${item.name}")
            return
        }

        val categoryText = categories.joinToString(", ") { it.name }
        player.sendMessage("Du hast ${PlainTextComponentSerializer.plainText().serialize(event.item.displayName())} gegessen. Kategorien: $categoryText")

    }
}