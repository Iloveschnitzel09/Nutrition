package de.schnitzel.nutrition.service

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.plugin
import de.schnitzel.nutrition.util.getNuScore
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.minutes


object HungerService {

    private lateinit var job: Job
    private val nuScores = mutableMapOf<UUID, Int>()

    fun start() {
        job = plugin.launch {
            while (true) {
                tick()

                applyEffect()
                delay(5.minutes)
            }
        }
    }


    suspend fun tick() {
        server.onlinePlayers.forEach { player ->
            val data = DatabaseService.loadNutritionData(player.uniqueId)

            data.meat = (data.meat - 1).coerceAtLeast(0)
            data.fruit = (data.fruit - 1).coerceAtLeast(0)
            data.dairy = (data.dairy - 1).coerceAtLeast(0)
            data.cereals = (data.cereals - 1).coerceAtLeast(0)
            data.sugar = (data.sugar - 1).coerceAtLeast(0)
            data.nuScore = getNuScore(data)

            nuScores[player.uniqueId] = data.nuScore

            DatabaseService.saveNutritionData(data)
        }
    }

    /*
     * Danke an ChatGPT fir das +*when** hatte kein Bock die Wahrscheinlichkeiten und effekte selbst auszudenken :D
     */

    suspend fun applyEffect() {
        server.onlinePlayers.forEach { player ->
            val nuScore = nuScores[player.uniqueId] ?: return@forEach
            withContext(plugin.entityDispatcher(player)){
                when (nuScore) {
                    in 7 until 10 -> {
                        // überwiegend gute/positive Effekte, starke Boni sind selten
                        val r = Random.nextDouble()

                        player.clearActivePotionEffects()
                        when {
                            r < 0.60 -> {
                                // normal gute/leichte Boni (häufig)
                                player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 1500, 0))
                            }

                            r < 0.85 -> {
                                // seltener: starker, aber kurz
                                player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 1500, 1))
                                player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 1500, 1))
                            }

                            else -> {
                                // sehr seltenes Glück (kleiner Buff ohne Heilung)
                                player.addPotionEffect(PotionEffect(PotionEffectType.LUCK, 1500, 0))
                                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 1500, 0))
                            }
                        }
                    }

                    in 5 until 7 -> {
                        // neutral / leicht positiv — zuverlässig, nicht zu stark
                        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 1500, 0))
                        // kleine Chance auf einen zusätzlichen, seltenen Bonus
                        if (Random.nextDouble() < 0.15) {
                            player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 1500, 0))
                        }
                    }

                    in 3 until 5 -> {
                        // leicht ins negative: kleine Nachteile / Unpässlichkeiten
                        player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, 1500, 0))
                        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 1500, 0))
                        // kleine Chance auf Verwirrung (z.B. optische Störung)
                        if (Random.nextDouble() < 0.20) {
                            player.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 1500, 0))
                        }
                    }

                    in 1 until 3 -> {
                        // deutlich negativ: härtere, aber kurzzeitige Strafen
                        player.addPotionEffect(PotionEffect(PotionEffectType.POISON, 1500, 0))
                        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 1500, 0))
                        player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 1500, 0))
                        // sehr kleine Chance auf Mining Fatigue statt Poison (Varianz)
                        if (Random.nextDouble() < 0.10) {
                            player.removePotionEffect(PotionEffectType.POISON) // ersetze Poison durch Fatigue
                            player.addPotionEffect(PotionEffect(PotionEffectType.MINING_FATIGUE, 1500, 0))
                        }
                    }

                    else -> {
                        // fallback: falls Score außerhalb erwartetem Bereich ist
                        player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 1500, 0))
                    }
                }
            }
        }
    }

    fun updatePlayerCache(player: Player) {
        plugin.launch {
            val data = DatabaseService.loadNutritionData(player.uniqueId)
            nuScores[player.uniqueId] = data.nuScore
        }
    }

    fun removePlayerCache(player: Player) {
        nuScores.remove(player.uniqueId)
    }

    fun stop() {
        job.cancel()
    }
}