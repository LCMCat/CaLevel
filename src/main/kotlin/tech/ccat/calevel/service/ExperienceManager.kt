package tech.ccat.calevel.service

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.ccat.calevel.config.ConfigManager
import tech.ccat.calevel.event.ExperienceUpdateEvent
import tech.ccat.calevel.provider.ExperienceProvider
import java.util.concurrent.CopyOnWriteArrayList

class ExperienceManager(
    private val playerCache: PlayerLevelCache,
    private val configManager: ConfigManager
) {
    private val providers = CopyOnWriteArrayList<ExperienceProvider>()

    fun registerProvider(provider: ExperienceProvider) {
        providers.add(provider)
    }

    fun unregisterProvider(provider: ExperienceProvider) {
        providers.remove(provider)
    }

    fun updatePlayerExperience(player: Player) {
        val uuid = player.uniqueId
        val data = playerCache.getPlayerData(uuid) ?: return

        // 计算总经验值
        val totalExp = calculateTotalExperience(player)

        // 更新玩家数据
        data.exp = totalExp

        // 触发经验更新事件
        Bukkit.getPluginManager().callEvent(ExperienceUpdateEvent(player, totalExp))
    }

    private fun calculateTotalExperience(player: Player): Int {
        return providers.sumOf { it.getExperienceData(player).currentExp }
    }
}