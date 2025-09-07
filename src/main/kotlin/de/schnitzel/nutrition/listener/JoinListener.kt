package de.schnitzel.nutrition.listener

import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import kotlinx.coroutines.Dispatchers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId
        plugin.launch(Dispatchers.IO) {
            if (!DatabaseService.isPlayerInDatabase(uuid)) {
                DatabaseService.initNutritionData(uuid)
            }
        }
    }
}