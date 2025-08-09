package tech.ccat.calevel.provider

import org.bukkit.entity.Player
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.config.RewardConfig
import tech.ccat.kstats.api.StatProvider
import tech.ccat.kstats.model.PlayerStat

class LevelRewardStatProvider : StatProvider {
    private val config: RewardConfig
        get() = CaLevel.instance.configManager.rewardConfig

    override fun provideStats(player: Player): PlayerStat {
        val levelData = CaLevel.instance.playerCache.getPlayerData(player.uniqueId) ?: return PlayerStat()

        // 只计算已领取奖励的等级
        val claimedLevels = levelData.claimedRewards

        // 计算生命值奖励（每级）
        val healthBonus = claimedLevels.size * config.healthPerLevel

        // 计算力量奖励（每5级）
        val strengthBonus = claimedLevels.count { it % 5 == 0 } * config.strengthPer5Levels

        return PlayerStat().apply {
            health = healthBonus.toDouble()
            strength = strengthBonus.toDouble()
        }
    }

    companion object {
        fun registerToKStats() {
            val service = CaLevel.getKStatsAPI()
            service?.registerProvider(LevelRewardStatProvider())
        }

        fun unregisterFromKStats() {
            val service = CaLevel.getKStatsAPI()
            service?.registeredProviders
                ?.filterIsInstance<LevelRewardStatProvider>()
                ?.forEach { service.unregisterProvider(it) }
        }
    }
}