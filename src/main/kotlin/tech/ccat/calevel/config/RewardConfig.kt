package tech.ccat.calevel.config

import org.bukkit.configuration.ConfigurationSection

class RewardConfig(private val config: ConfigurationSection) {
    val healthPerLevel: Int
        get() = config.getInt("health-per-level", 1)

    val strengthPer5Levels: Int
        get() = config.getInt("strength-per-5-levels", 1)
}