package tech.ccat.calevel.service

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import tech.ccat.calevel.config.ConfigManager
import tech.ccat.calevel.event.ExperienceUpdateEvent
import tech.ccat.calevel.model.ExpCategory
import tech.ccat.calevel.model.ExpProviderData
import tech.ccat.calevel.provider.ExperienceProvider
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class ExperienceManager(
    private val playerCache: PlayerLevelCache,
    private val configManager: ConfigManager
) {
    private val logger = Bukkit.getLogger()

    // 按类别存储提供器
    private val providersByCategory = ConcurrentHashMap<ExpCategory, CopyOnWriteArrayList<ExperienceProvider>>()

    /**
     * 注册经验值提供器
     * @param provider 要注册的经验值提供器
     */
    fun registerProvider(provider: ExperienceProvider) {
        val category = provider.getCategory()
        val providers = providersByCategory.computeIfAbsent(category) { CopyOnWriteArrayList() }
        providers.add(provider)
        logger.fine("注册经验提供器: ${provider.getName()} (类别: ${category.displayName})")
    }

    /**
     * 取消注册经验值提供器
     * @param provider 要取消注册的经验值提供器
     */
    fun unregisterProvider(provider: ExperienceProvider) {
        val category = provider.getCategory()
        val providers = providersByCategory[category]
        providers?.remove(provider)
        logger.fine("取消注册经验提供器: ${provider.getName()} (类别: ${category.displayName})")
    }

    /**
     * 获取指定类别的所有经验值提供器
     * @param category 经验类别
     * @return 该类别下的经验值提供器列表
     */
    fun getProvidersByCategory(category: ExpCategory): List<ExperienceProvider> {
        return providersByCategory[category]?.toList() ?: emptyList()
    }

    /**
     * 获取所有经验值提供器
     * @return 所有经验值提供器列表
     */
    fun getAllProviders(): List<ExperienceProvider> {
        return providersByCategory.values.flatMap { it.toList() }
    }

    /**
     * 更新玩家经验值
     * @param player 目标玩家
     */
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

    /**
     * 计算玩家总经验值
     * @param player 目标玩家
     * @return 总经验值
     */
    private fun calculateTotalExperience(player: Player): Int {
        return getAllProviders().sumOf { it.getExperienceData(player).currentExp }
    }

    /**
     * 获取玩家指定类别的经验数据
     * @param player 目标玩家
     * @param category 经验类别
     * @return 该类别下的经验数据列表
     */
    fun getExperienceDataByCategory(player: Player, category: ExpCategory): List<ExpProviderData> {
        return getProvidersByCategory(category).map { it.getExperienceData(player) }
    }
}