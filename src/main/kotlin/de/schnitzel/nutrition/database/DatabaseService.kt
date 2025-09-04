package de.schnitzel.nutrition.database

import de.schnitzel.nutrition.NutritionData
import de.schnitzel.nutrition.database.tables.PlayerNutritionEntryTable
import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.database.database.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Path
import java.util.UUID

object DatabaseService {

    lateinit var databaseProvider: DatabaseProvider

    fun establishConnection(path: Path) {
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

    suspend fun saveNutritionData(data: NutritionData) = newSuspendedTransaction(Dispatchers.IO) {
        PlayerNutritionEntryTable.insert {
            it[playerUuid] = data.playerUuid
            it[nuScore] = data.nuScore
            it[fruit] = data.fruit
            it[sugar] = data.sugar
            it[cereals] = data.cereals
            it[meat] = data.meat
            it[dairy] = data.dairy
        }
    }

    suspend fun loadNutritionData(playerUuid: UUID) = newSuspendedTransaction(Dispatchers.IO) {
        PlayerNutritionEntryTable.selectAll().where(PlayerNutritionEntryTable.playerUuid eq playerUuid).map {
            NutritionData(
                playerUuid = it[PlayerNutritionEntryTable.playerUuid],
                nuScore = it[PlayerNutritionEntryTable.nuScore],
                fruit = it[PlayerNutritionEntryTable.fruit],
                sugar = it[PlayerNutritionEntryTable.sugar],
                cereals = it[PlayerNutritionEntryTable.cereals],
                meat = it[PlayerNutritionEntryTable.meat],
                dairy = it[PlayerNutritionEntryTable.dairy]
            )
        }.firstOrNull()
    }
}