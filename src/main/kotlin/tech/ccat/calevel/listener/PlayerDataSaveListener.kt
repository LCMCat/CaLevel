package tech.ccat.calevel.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.event.ExperienceUpdateEvent
import tech.ccat.calevel.util.MessageFormatter

class PlayerDataSaveListener(private val plugin: CaLevel) : Listener {
    private val logger = plugin.logger

    @EventHandler
    fun onExperienceUpdate(event: ExperienceUpdateEvent) {
        val player = event.player
        val uuid = player.uniqueId

        // 获取玩家数据
        val data = plugin.playerCache.getPlayerData(uuid) ?: run {
            logger.warning("无法保存玩家数据: ${player.name} (UUID: $uuid) - 数据未加载")
            return
        }

        // 异步保存数据到数据库
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                plugin.playerDao.savePlayerData(data)
            } catch (e: Exception) {
                logger.severe("保存玩家数据失败: ${player.name} (UUID: $uuid)")
                logger.severe("错误信息: ${e.message}")
                e.printStackTrace()

                // 通知玩家（如果在线）
                if (player.isOnline) {
                    player.sendMessage(MessageFormatter.format("data-save-failed"))
                }
            }
        })
    }
}