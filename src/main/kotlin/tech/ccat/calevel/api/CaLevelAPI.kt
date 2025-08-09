package tech.ccat.calevel.api

import org.bukkit.entity.Player
import tech.ccat.calevel.model.PlayerLevelData
import tech.ccat.calevel.provider.ExperienceProvider

interface CaLevelAPI {
    /**
     * 注册经验值提供器
     * @param provider 经验值提供器实例
     */
    fun registerExperienceProvider(provider: ExperienceProvider)

    /**
     * 取消注册经验值提供器
     * @param provider 经验值提供器实例
     */
    fun unregisterExperienceProvider(provider: ExperienceProvider)

    /**
     * 注册自定义等级奖励
     * @param reward 自定义等级奖励实例
     */
    fun registerCustomReward(reward: CustomLevelReward)

    /**
     * 取消注册自定义等级奖励
     * @param reward 自定义等级奖励实例
     */
    fun unregisterCustomReward(reward: CustomLevelReward)

    /**
     * 获取玩家等级数据
     * @param player 目标玩家
     * @return 玩家等级数据，如果不存在则返回null
     */
    fun getPlayerLevelData(player: Player): PlayerLevelData?

    /**
     * 强制更新玩家经验值
     * @param player 目标玩家
     */
    fun updatePlayerExperience(player: Player)

    /**
     * 获取玩家所有可领取的奖励等级
     * @param player 目标玩家
     * @return 可领取的等级列表
     */
    fun getClaimableRewards(player: Player): List<Int>

    /**
     * 获取玩家已领取的奖励等级
     * @param player 目标玩家
     * @return 已领取的等级列表
     */
    fun getClaimedRewards(player: Player): List<Int>

    /**
     * 领取指定等级的奖励
     * @param player 目标玩家
     * @param level 要领取的等级
     * @return 是否成功领取
     */
    fun claimReward(player: Player, level: Int): Boolean

    /**
     * 领取所有可领取的奖励
     * @param player 目标玩家
     * @return 成功领取的奖励数量
     */
    fun claimAllRewards(player: Player): Int
}