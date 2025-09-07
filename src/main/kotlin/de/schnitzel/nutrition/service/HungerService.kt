package de.schnitzel.nutrition.service

import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import de.schnitzel.nutrition.util.getNuScore
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object HungerService {

    private lateinit var job: Job

    fun start() {
        job = plugin.launch {
            while (true) {
                tick()
                delay(5.minutes)
            }
        }
    }

    suspend fun tick() {
        /*       val location: Location

             withContext(plugin.regionDispatcher(location)) {
                   location.block.type = Material.AIR
               }

               withContext(plugin.entityDispatcher(player)) {
                    player.health = 0.0
                }*/

        server.onlinePlayers.forEach { player ->
            val data = DatabaseService.loadNutritionData(player.uniqueId)

            data.meat = (data.meat - 1).coerceAtLeast(0)
            data.fruit = (data.fruit - 1).coerceAtLeast(0)
            data.dairy = (data.dairy - 1).coerceAtLeast(0)
            data.cereals = (data.cereals - 1).coerceAtLeast(0)
            data.sugar = (data.sugar - 1).coerceAtLeast(0)
            data.nuScore = getNuScore(data)
            DatabaseService.saveNutritionData(data)
        }
    }

    fun stop() {
        job.cancel()
    }

}