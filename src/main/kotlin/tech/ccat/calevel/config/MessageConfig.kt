package tech.ccat.calevel.config

import org.bukkit.configuration.ConfigurationSection

class MessageConfig(private val config: ConfigurationSection) {
    fun getMessage(key: String, vararg args: Any): String {
        val raw = config.getString(key, "")?.replace("&", "§") ?: ""
        return if (args.isNotEmpty()) String.format(raw, *args) else raw
    }
}