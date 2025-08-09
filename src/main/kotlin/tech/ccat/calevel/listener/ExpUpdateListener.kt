package tech.ccat.calevel.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.event.ExperienceUpdateEvent
import tech.ccat.calevel.event.LevelUpEvent

class ExpUpdateListener(private val plugin: CaLevel) : Listener {
    @EventHandler
    fun onExperienceUpdate(event: ExperienceUpdateEvent) {
        val player = event.player
        val uuid = player.uniqueId

        val data = plugin.playerCache.getPlayerData(uuid) ?: return
        val oldLevel = data.level

        // 计算新等级 (每100经验1级)
        val newLevel = event.exp / 100

        // 更新玩家数据
        data.exp = event.exp
        data.level = newLevel

        // 如果等级提升，触发升级事件
        if (newLevel > oldLevel) {
            Bukkit.getPluginManager().callEvent(
                LevelUpEvent(player, oldLevel, newLevel, event.exp)
            )
        }
    }
}