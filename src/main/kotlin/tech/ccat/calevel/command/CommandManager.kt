package tech.ccat.calevel.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import tech.ccat.calevel.util.MessageFormatter

class CommandManager : TabExecutor {
    private val commands = mutableMapOf<String, AbstractCommand>()

    fun register(command: AbstractCommand) {
        commands[command.name] = command
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sendSenderHelp(sender)
            return true
        }

        val subCommand = commands[args[0].lowercase()] ?: run {
            sendSenderHelp(sender)
            return true
        }

        if (subCommand.playerOnly && sender !is Player) {
            sender.sendMessage(MessageFormatter.format("player-command"))
            return true
        }

        if (subCommand.permission != null && !sender.hasPermission(subCommand.permission)) {
            sender.sendMessage(MessageFormatter.format("no-permission"))
            return true
        }

        if (args.size - 1 < subCommand.minArgs) {
            sender.sendMessage(MessageFormatter.format("command-usage", subCommand.usage))
            return true
        }

        return subCommand.execute(sender, args)
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, alias: String, args: Array<out String>): List<String> {
        if (args.size == 1) {
            return getAvailableCommands(sender)
                .map { it.name }
                .filter { it.startsWith(args[0], ignoreCase = true) }
        }

        val subCommand = commands[args[0].lowercase()] ?: return emptyList()

        if ((subCommand.playerOnly && sender !is Player) ||
            (subCommand.permission != null && !sender.hasPermission(subCommand.permission))) {
            return emptyList()
        }

        return subCommand.onTabComplete(sender, args)
    }

    private fun sendSenderHelp(sender: CommandSender) {
        val available = getAvailableCommands(sender)
        if (available.isEmpty()) {
            sender.sendMessage(MessageFormatter.format("no-available-command"))
        } else {
            val usage = available.joinToString("\n") { it.usage }
            sender.sendMessage(MessageFormatter.format("command-list-header") + "\n$usage")
        }
    }

    private fun getAvailableCommands(sender: CommandSender): List<AbstractCommand> {
        return commands.values.filter { command ->
            (!command.playerOnly || sender is Player) &&
                    (command.permission == null || sender.hasPermission(command.permission))
        }
    }
}