package de.schnitzel.nutrition.command

import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import de.schnitzel.nutrition.util.NutritionData
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor


fun testCommand() = commandAPICommand("test") {
    withPermission("abc.command.test")
    playerExecutor { player, _ ->
        plugin.launch {
            DatabaseService.saveNutritionData(
                NutritionData (
                    player.uniqueId, 1, 1, 1, 1, 1, 1
                )
            )
        }
    }
}