package tech.ccat.calevel.config

import org.bukkit.configuration.ConfigurationSection

class PluginConfig(private val config: ConfigurationSection) {
    val mongoUri: String
        get() = config.getString("mongo.uri", "mongodb://localhost:27017") ?: "mongodb://localhost:27017"

    val mongoDatabase: String
        get() = config.getString("mongo.database", "CatTitanium") ?: "CatTitanium"

    val mongoCollection: String
        get() = config.getString("mongo.collection", "PlayerLevelCollection") ?: "PlayerLevelCollection"
}