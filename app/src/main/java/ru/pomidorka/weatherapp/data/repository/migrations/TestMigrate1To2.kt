package ru.pomidorka.weatherapp.data.repository.migrations

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

class TestMigrate1To2 : RepositoryMigrateStrategy {
    override val fromVersion = 1
    override val toVersion = 2

    override fun migrate(oldJson: JsonElement): JsonElement {
        return buildJsonObject {
            put("versionData", toVersion)
            put("selectedCity", oldJson.jsonObject["selectedCity"]!!)
            put("favoritesCity", oldJson.jsonObject["favoritesCity"]!!.jsonArray)
        }
    }
}