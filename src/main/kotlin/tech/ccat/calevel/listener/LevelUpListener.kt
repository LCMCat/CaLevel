package tech.ccat.calevel.listener

import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.event.LevelUpEvent
import tech.ccat.calevel.util.MessageFormatter

class LevelUpListener(private val plugin: CaLevel) : Listener {
    @EventHandler
    fun onLevelUp(event: LevelUpEvent) {
        val player = event.player
        val newLevel = event.newLevel

        // 发送升级提示
        player.sendMessage(MessageFormatter.format("level-up", newLevel))

        // 如果玩家在线，可以添加其他效果（如粒子效果、声音等）
         player.world.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }
}