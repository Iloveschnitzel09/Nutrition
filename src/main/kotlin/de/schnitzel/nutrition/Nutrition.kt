package de.schnitzel.nutrition

import de.schnitzel.nutrition.command.testCommand
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.listener.EatListener
import de.schnitzel.nutrition.service.FoodService
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
        FoodService.load(this)


        testCommand()
        manager.registerEvents(EatListener(), this)
    }

    override fun onDisable() {
        DatabaseService.closeConnection()
    }
}
