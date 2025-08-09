package tech.ccat.calevel.service

import org.bukkit.entity.Player
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.api.CustomLevelReward
import tech.ccat.calevel.config.ConfigManager
import tech.ccat.calevel.config.RewardConfig
import tech.ccat.calevel.model.PlayerLevelData
import java.util.concurrent.CopyOnWriteArrayList

class RewardManager(
    private val playerCache: PlayerLevelCache,
    private val configManager: ConfigManager
) {
    private val customRewards = CopyOnWriteArrayList<CustomLevelReward>()
    private val config: RewardConfig
        get() = configManager.rewardConfig

    fun registerCustomReward(reward: CustomLevelReward) {
        customRewards.add(reward)
    }

    fun unregisterCustomReward(reward: CustomLevelReward) {
        customRewards.remove(reward)
    }

    fun getPlayerLevelData(player: Player): PlayerLevelData? {
        return playerCache.getPlayerData(player.uniqueId)
    }

    fun getUnclaimedLevels(player: Player): List<Int> {
        val data = getPlayerLevelData(player) ?: return emptyList()
        return (1..data.level).filter { level ->
            !data.hasClaimedReward(level) && isRewardLevel(level)
        }
    }
    /**
     * 获取玩家可领取的奖励等级
     * @param player 目标玩家
     * @return 可领取的等级列表
     */
    fun getClaimableRewards(player: Player): List<Int> {
        val data = getPlayerLevelData(player) ?: return emptyList()
        return (1..data.level).filter { level ->
            !data.hasClaimedReward(level) && isRewardLevel(level)
        }
    }

    /**
     * 获取玩家已领取的奖励等级
     * @param player 目标玩家
     * @return 已领取的等级列表
     */
    fun getClaimedRewards(player: Player): List<Int> {
        val data = getPlayerLevelData(player) ?: return emptyList()
        return data.claimedRewards.toList()
    }

    /**
     * 领取指定等级的奖励
     * @param player 目标玩家
     * @param level 要领取的等级
     * @return 是否成功领取
     */
    fun claimReward(player: Player, level: Int): Boolean {
        val data = getPlayerLevelData(player) ?: return false

        // 检查是否可以领取该等级奖励
        if (level > data.level || data.hasClaimedReward(level) || !isRewardLevel(level)) {
            return false
        }

        // 应用基础奖励
        applyBaseRewards(player, level)

        // 应用自定义奖励
        applyCustomRewards(player, level)

        // 标记为已领取
        data.claimReward(level)

        // 更新KStats属性
        CaLevel.getKStatsAPI()?.forceUpdate(player)

        return true
    }

    /**
     * 领取所有可领取的奖励
     * @param player 目标玩家
     * @return 成功领取的奖励数量
     */
    fun claimAllRewards(player: Player): Int {
        val claimableLevels = getClaimableRewards(player)
        var claimedCount = 0

        claimableLevels.forEach { level ->
            if (claimReward(player, level)) {
                claimedCount++
            }
        }

        return claimedCount
    }

    private fun isRewardLevel(level: Int): Boolean {
        // 每级都有生命值奖励，每5级有力量奖励
        return level % 1 == 0 || level % 5 == 0
    }

    private fun applyBaseRewards(player: Player, level: Int) {
        // 基础奖励逻辑
        // 实际效果通过LevelRewardStatProvider实现
    }

    private fun applyCustomRewards(player: Player, level: Int) {
        customRewards.forEach { reward ->
            if (level in reward.getApplicableLevels()) {
                reward.applyReward(player, level)
            }
        }
    }


}