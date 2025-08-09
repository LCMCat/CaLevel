package tech.ccat.calevel.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.util.MessageFormatter

class SelfCheckCommand : AbstractCommand(
    name = "self",
    usage = "/level",
    playerOnly = true
) {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        val player = sender as Player
        val data = CaLevel.instance.playerCache.getPlayerData(player.uniqueId) ?: run {
            sender.sendMessage(MessageFormatter.format("data-not-loaded"))
            return true
        }

        val expToNextLevel = (data.level + 1) * 100 - data.exp
        sender.sendMessage(
            MessageFormatter.format(
                "level-show-self",
                data.level,
                data.exp,
                expToNextLevel
            )
        )
        return true
    }
}