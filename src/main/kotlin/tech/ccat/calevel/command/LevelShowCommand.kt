package tech.ccat.calevel.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.util.MessageFormatter

class LevelShowCommand : AbstractCommand(
    name = "show",
    permission = "calevel.admin",
    usage = "/level show <玩家名>",
    minArgs = 1
) {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        val targetName = args[1]
        val target = Bukkit.getPlayer(targetName) ?: run {
            sender.sendMessage(MessageFormatter.format("player-not-found", targetName))
            return true
        }

        val data = CaLevel.instance.playerCache.getPlayerData(target.uniqueId) ?: run {
            sender.sendMessage(MessageFormatter.format("data-not-loaded"))
            return true
        }

        val expToNextLevel = (data.level + 1) * 100 - data.exp
        sender.sendMessage(
            MessageFormatter.format(
                "level-show-other",
                target.name,
                data.level,
                data.exp,
                expToNextLevel
            )
        )
        return true
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return if (args.size == 2) {
            Bukkit.getOnlinePlayers().map { it.name }
        } else {
            emptyList()
        }
    }
}