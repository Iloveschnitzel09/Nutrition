package de.schnitzel.nutrition

import de.schnitzel.nutrition.command.nutritionGuiCommand
import de.schnitzel.nutrition.command.testCommand
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.listener.EatListener
import de.schnitzel.nutrition.listener.JoinListener
import de.schnitzel.nutrition.service.FoodService
import de.schnitzel.nutrition.service.HungerService
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(Nutrition::class.java)

class Nutrition : JavaPlugin() {

    override fun onLoad() {
        DatabaseService.establishConnection(plugin.dataPath)
        DatabaseService.createTables()
    }

    override fun onEnable() {
        val manager = server.pluginManager

        // Plugin startup logic
        saveDefaultConfig()
        FoodService.load()
        HungerService.start()

        nutritionGuiCommand()
        testCommand()
        manager.registerEvents(EatListener(), this)
        manager.registerEvents(JoinListener(), this)
    }

    override fun onDisable() {
        DatabaseService.closeConnection()
        HungerService.stop()
    }
}

/**
 * NUTRITION CORE x SIMPLE CORE
 *
 * Gutes Plugin, paar Sachen kann man aber überarbeiten. Siehe "//TODO: "
 * 3/5 STERNE
 */