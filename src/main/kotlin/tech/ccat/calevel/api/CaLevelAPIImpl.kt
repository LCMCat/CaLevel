package tech.ccat.calevel.api

import org.bukkit.entity.Player
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.model.PlayerLevelData
import tech.ccat.calevel.provider.ExperienceProvider

class CaLevelAPIImpl(private val plugin: CaLevel) : CaLevelAPI {
    override fun registerExperienceProvider(provider: ExperienceProvider) {
        plugin.expManager.registerProvider(provider)
    }

    override fun unregisterExperienceProvider(provider: ExperienceProvider) {
        plugin.expManager.unregisterProvider(provider)
    }

    override fun registerCustomReward(reward: CustomLevelReward) {
        plugin.rewardManager.registerCustomReward(reward)
    }

    override fun unregisterCustomReward(reward: CustomLevelReward) {
        plugin.rewardManager.unregisterCustomReward(reward)
    }

    override fun getPlayerLevelData(player: Player): PlayerLevelData? {
        return plugin.playerCache.getPlayerData(player.uniqueId)
    }

    override fun updatePlayerExperience(player: Player) {
        plugin.expManager.updatePlayerExperience(player)
    }

    override fun getClaimableRewards(player: Player): List<Int> {
        return plugin.rewardManager.getClaimableRewards(player)
    }

    override fun getClaimedRewards(player: Player): List<Int> {
        return plugin.rewardManager.getClaimedRewards(player)
    }

    override fun claimReward(player: Player, level: Int): Boolean {
        return plugin.rewardManager.claimReward(player, level)
    }

    override fun claimAllRewards(player: Player): Int {
        return plugin.rewardManager.claimAllRewards(player)
    }
}