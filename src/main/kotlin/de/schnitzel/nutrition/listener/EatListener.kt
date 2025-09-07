package de.schnitzel.nutrition.listener

import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import de.schnitzel.nutrition.service.FoodService
import de.schnitzel.nutrition.util.getNuScore
import dev.slne.surf.surfapi.core.api.messages.ComponentMessage
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

class EatListener : Listener {

    @EventHandler
    fun onEat(event: PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item.type

        updateNutritionData(player, item)
    }

    @EventHandler
    fun onCakeEat(event: PlayerInteractEvent){
        val player = event.player
        val item: Material = event.clickedBlock?.type ?: Material.AIR

        if(event.action != Action.RIGHT_CLICK_BLOCK || item != Material.CAKE) return

        updateNutritionData(player, item)
    }

    fun updateNutritionData(player: Player, item: Material){
        val nutritionData = FoodService.getNutritionDataForItem(player, item)

        plugin.launch(Dispatchers.IO) {
            var data = DatabaseService.loadNutritionData(player.uniqueId)

            if (data != null) {
                data.fruit = (data.fruit + nutritionData.fruit).coerceAtMost(10)
                data.sugar = (data.sugar + nutritionData.sugar).coerceAtMost(10)
                data.cereals = (data.cereals + nutritionData.cereals).coerceAtMost(10)
                data.meat = (data.meat + nutritionData.meat).coerceAtMost(10)
                data.dairy = (data.dairy + nutritionData.dairy).coerceAtMost(10)

                data.nuScore = getNuScore(data)
                DatabaseService.saveNutritionData(data)


            }
            data = DatabaseService.loadNutritionData(player.uniqueId)
            player.sendText {
                info(data?.fruit.toString() + " " + data?.sugar.toString() + " " + data?.cereals.toString() + " " + data?.meat.toString() + " " + data?.dairy.toString())
            }

        }
    }
}