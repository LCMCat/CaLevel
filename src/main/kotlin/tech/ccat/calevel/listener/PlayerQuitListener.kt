package tech.ccat.calevel.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import tech.ccat.calevel.CaLevel

class PlayerQuitListener(private val plugin: CaLevel) : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val uuid = player.uniqueId

        // 保存数据到数据库
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            plugin.playerCache.getPlayerData(uuid)?.let {
               try {
                    plugin.playerDao.savePlayerData(it)
                  plugin.logger.fine("已保存退出玩家数据: ${player.name} (等级: ${it.level}, 经验: ${it.exp})")
                } catch (e: Exception) {
                  plugin.logger.severe("保存退出玩家数据失败: ${player.name} (UUID: $uuid)")
                  plugin.logger.severe("错误信息: ${e.message}")
                   e.printStackTrace()
               }
            }
        })

        // 从缓存移除
        plugin.playerCache.removePlayerData(uuid)
    }
}