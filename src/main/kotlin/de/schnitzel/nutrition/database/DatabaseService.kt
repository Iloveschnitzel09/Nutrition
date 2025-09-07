package de.schnitzel.nutrition.database

import de.schnitzel.nutrition.database.tables.PlayerNutritionEntryTable
import de.schnitzel.nutrition.util.NutritionData
import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.database.database.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.nio.file.Path
import java.util.*

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

    suspend fun initNutritionData(uuid: UUID) = newSuspendedTransaction(Dispatchers.IO) {
        PlayerNutritionEntryTable.insert {
            it[playerUuid] = uuid
            it[nuScore] = 10
            it[fruit] = 10
            it[sugar] = 10
            it[cereals] = 10
            it[meat] = 10
            it[dairy] = 10
        }
    }

    suspend fun isPlayerInDatabase(playerUuid: UUID) = newSuspendedTransaction(Dispatchers.IO) {
        PlayerNutritionEntryTable.selectAll().where(PlayerNutritionEntryTable.playerUuid eq playerUuid).count() > 0
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

    suspend fun saveNutritionData(data: NutritionData) = newSuspendedTransaction(Dispatchers.IO) {
        PlayerNutritionEntryTable.update(where = { PlayerNutritionEntryTable.playerUuid eq data.playerUuid }) {
            it[nuScore] = data.nuScore
            it[fruit] = data.fruit
            it[sugar] = data.sugar
            it[cereals] = data.cereals
            it[meat] = data.meat
            it[dairy] = data.dairy
        }
    }
}