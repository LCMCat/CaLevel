package tech.ccat.calevel.api

import org.bukkit.entity.Player

interface CustomLevelReward {
    /**
     * 获取奖励适用的等级
     * @return 等级列表
     */
    fun getApplicableLevels(): List<Int>

    /**
     * 应用奖励给玩家
     * @param player 目标玩家
     * @param level 奖励等级
     */
    fun applyReward(player: Player, level: Int)

    /**
     * 获取奖励描述
     * @return 奖励描述
     */
    fun getDescription(): String
}