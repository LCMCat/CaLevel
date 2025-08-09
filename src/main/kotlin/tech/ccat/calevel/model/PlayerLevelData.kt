package tech.ccat.calevel.model

import java.util.*

data class PlayerLevelData(
    val uuid: UUID,
    var exp: Int = 0,
    var level: Int = 0,
    val claimedRewards: MutableList<Int> = mutableListOf()
) {
    fun addExp(amount: Int) {
        exp += amount
    }

    fun claimReward(level: Int) {
        if (!claimedRewards.contains(level)) {
            claimedRewards.add(level)
            // 排序以便于显示
            claimedRewards.sort()
        }
    }

    fun hasClaimedReward(level: Int): Boolean {
        return claimedRewards.contains(level)
    }
}