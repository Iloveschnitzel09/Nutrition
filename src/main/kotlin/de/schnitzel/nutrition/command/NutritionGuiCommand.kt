package de.schnitzel.nutrition.command

import de.schnitzel.nutrition.gui.NewNutritionGui
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Material

fun nutritionGuiCommand() = commandAPICommand("nutritiongui") {
    withPermission("abc.command.nutritiongui")
    playerExecutor { player, _ ->

        val material = player.inventory.itemInMainHand.type

        if(material == Material.AIR){
            player.sendText {
                error("Du hast kein Gegenstand in der Hand!")
            }
            return@playerExecutor
        }
        NewNutritionGui().show(player)
    }

}