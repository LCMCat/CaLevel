package tech.ccat.calevel.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class LevelUpEvent(
    val player: Player,
    val oldLevel: Int,
    val newLevel: Int,
    val currentExp: Int
) : Event() {
    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlers
    }

    override fun getHandlers(): HandlerList {
        return getHandlerList()
    }
}