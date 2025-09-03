package de.schnitzel.nutrition.database

import de.schnitzel.nutrition.database.tables.PlayerNutritionEntryTable
import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.database.database.DatabaseProvider
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseService {

    lateinit var databaseProvider: DatabaseProvider

    fun establishConnection(path: java.nio.file.Path) {
        databaseProvider = DatabaseManager(path, path).databaseProvider
        databaseProvider.connect()
    }

    fun closeConnection() {
        databaseProvider.disconnect()
    }

    fun createTables() {
        transaction {
            SchemaUtils.create(PlayerNutritionEntryTable)
        }
    }
}