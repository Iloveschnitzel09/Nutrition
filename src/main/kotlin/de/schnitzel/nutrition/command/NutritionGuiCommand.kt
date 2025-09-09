package de.schnitzel.nutrition.command

import de.schnitzel.nutrition.gui.NutritionGui
import de.schnitzel.nutrition.permissions.PermissionRegistry
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Material

fun nutritionGuiCommand() = commandAPICommand("nutritiongui") {
    withPermission(PermissionRegistry.COMMAND_NUTRITION_GUI)
    playerExecutor { player, _ ->
        NutritionGui(player).show(player)
    }

}