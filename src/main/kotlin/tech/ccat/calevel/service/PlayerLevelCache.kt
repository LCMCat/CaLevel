package tech.ccat.calevel.service

import tech.ccat.calevel.dao.PlayerLevelDao
import tech.ccat.calevel.model.PlayerLevelData
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerLevelCache {
    private val playerDataMap = ConcurrentHashMap<UUID, PlayerLevelData>()

    fun addPlayerData(uuid: UUID, data: PlayerLevelData) {
        playerDataMap[uuid] = data
    }

    fun getPlayerData(uuid: UUID): PlayerLevelData? {
        return playerDataMap[uuid]
    }

    fun removePlayerData(uuid: UUID) {
        playerDataMap.remove(uuid)
    }

    fun saveAllPlayers(dao: PlayerLevelDao) {
        playerDataMap.values.forEach { dao.savePlayerData(it) }
    }
}