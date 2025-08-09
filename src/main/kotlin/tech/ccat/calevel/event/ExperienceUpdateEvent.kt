package tech.ccat.calevel.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ExperienceUpdateEvent(
    val player: Player,
    val exp: Int
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