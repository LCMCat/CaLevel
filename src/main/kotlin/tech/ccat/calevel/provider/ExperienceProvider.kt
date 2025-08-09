package tech.ccat.calevel.provider

import org.bukkit.entity.Player
import tech.ccat.calevel.model.ExpCategory
import tech.ccat.calevel.model.ExpProviderData

interface ExperienceProvider {
    /**
     * 获取指定玩家的经验数据
     * @param player 目标玩家
     * @return 经验数据对象
     */
    fun getExperienceData(player: Player): ExpProviderData

    /**
     * 获取提供器所属的经验类别
     * @return 经验类别枚举
     */
    fun getCategory(): ExpCategory

    /**
     * 获取提供器名称
     * @return 提供器名称
     */
    fun getName(): String
}