package tech.ccat.calevel.config

import org.bukkit.configuration.file.YamlConfiguration
import tech.ccat.calevel.CaLevel
import java.io.File

class ConfigManager {
    private val plugin = CaLevel.instance

    private val configs = mutableMapOf<String, YamlConfiguration>()
    private val configFiles = mutableMapOf<String, File>()

    lateinit var pluginConfig: PluginConfig
    lateinit var rewardConfig: RewardConfig
    lateinit var messageConfig: MessageConfig

    fun setup() {
        saveDefaultConfig("config.yml")
        saveDefaultConfig("rewards.yml")
        saveDefaultConfig("messages.yml")
        reloadAll()
    }

    private fun saveDefaultConfig(fileName: String) {
        val file = File(plugin.dataFolder, fileName)
        if (!file.exists()) {
            plugin.saveResource(fileName, false)
        }
        configFiles[fileName] = file
        configs[fileName] = YamlConfiguration.loadConfiguration(file)
    }

    fun reloadAll() {
        configFiles.forEach { (name, file) ->
            configs[name] = YamlConfiguration.loadConfiguration(file)
        }

        pluginConfig = PluginConfig(configs["config.yml"]!!)
        rewardConfig = RewardConfig(configs["rewards.yml"]!!)
        messageConfig = MessageConfig(configs["messages.yml"]!!)
    }
}