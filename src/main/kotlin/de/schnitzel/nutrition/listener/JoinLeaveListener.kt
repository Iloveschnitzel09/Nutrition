package de.schnitzel.nutrition.listener

import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import de.schnitzel.nutrition.service.HungerService
import kotlinx.coroutines.Dispatchers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

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
        HungerService.updatePlayerCache(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent
    ) {
        val player = event.player
        HungerService.removePlayerCache(player)
    }
}