package tech.ccat.calevel.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.api.CaLevelAPI
import tech.ccat.calevel.util.MessageFormatter

class ClaimRewardCommand : AbstractCommand(
    name = "claim",
    usage = "/level claim [等级]",
    minArgs = 0,
    playerOnly = true
) {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        val player = sender as Player
        val api = CaLevel.instance.server.servicesManager.getRegistration(CaLevelAPI::class.java)?.provider ?: run {
            sender.sendMessage(MessageFormatter.format("api-not-available"))
            return true
        }

        if (args.size > 1) {
            // 领取指定等级奖励
            val level = try {
                args[1].toInt()
            } catch (_: NumberFormatException) {
                sender.sendMessage(MessageFormatter.format("invalid-level"))
                return true
            }

            if (api.claimReward(player, level)) {
                sender.sendMessage(MessageFormatter.format("reward-claimed", level))
            } else {
                sender.sendMessage(MessageFormatter.format("cannot-claim", level))
            }
        } else {
            // 领取所有可领取奖励
            val claimedCount = api.claimAllRewards(player)
            if (claimedCount > 0) {
                sender.sendMessage(MessageFormatter.format("all-rewards-claimed", claimedCount))
            } else {
                sender.sendMessage(MessageFormatter.format("no-rewards-to-claim"))
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        if (args.size == 2 && sender is Player) {
            val api = CaLevel.instance.server.servicesManager.getRegistration(CaLevelAPI::class.java)?.provider
            return api?.getClaimableRewards(sender)?.map { it.toString() } ?: emptyList()
        }
        return emptyList()
    }
}