package tech.ccat.calevel.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.model.PlayerLevelData

class PlayerLoginListener(private val plugin: CaLevel) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId

        // 从数据库加载数据
        val data = plugin.playerDao.getPlayerData(uuid) ?: PlayerLevelData(uuid)

        // 存入缓存
        plugin.playerCache.addPlayerData(uuid, data)

        // 更新经验值
        plugin.expManager.updatePlayerExperience(player)
    }
}