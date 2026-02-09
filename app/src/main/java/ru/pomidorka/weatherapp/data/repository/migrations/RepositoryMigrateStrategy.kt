package ru.pomidorka.weatherapp.data.repository.migrations

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

interface RepositoryMigrateStrategy {
    val fromVersion: Int
    val toVersion: Int

    fun migrate(oldJson: JsonElement): JsonElement
}

fun JsonElement.migrateToLastVersion(): JsonElement {
    var currentVersion = this.jsonObject["versionData"]!!.jsonPrimitive.int
    var json = this

    while (migrations.containsKey(currentVersion)) {
        val migration = migrations[currentVersion]!!
        json = migration.migrate(json)
        currentVersion = migration.toVersion
    }

    return json
}

val migrations = listOf<RepositoryMigrateStrategy>(
//    TestMigrate1To2(),
).associateBy { it.fromVersion }
