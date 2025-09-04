package de.schnitzel.nutrition.command

import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.NutritionData
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import kotlinx.coroutines.Dispatchers


fun testCommand() = commandAPICommand("test") {
    withPermission("abc.command.test")
    playerExecutor { player, _ ->
        val data = NutritionData(
            player.uniqueId, 20, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f
        )

        plugin.launch(Dispatchers.IO) {
            //DatabaseService.saveNutritionData(data)

            val loadedData = DatabaseService.loadNutritionData(player.uniqueId)
            player.sendMessage(loadedData?.nuScore.toString(), loadedData?.fruit.toString(), loadedData?.sugar.toString(), loadedData?.cereals.toString(), loadedData?.meat.toString(), loadedData?.dairy.toString())
        }
    }
}