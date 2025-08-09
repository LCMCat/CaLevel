package tech.ccat.calevel.dao

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.config.ConfigManager
import tech.ccat.calevel.config.PluginConfig
import tech.ccat.calevel.model.PlayerLevelData
import java.util.*

class PlayerLevelDao(private val configManager: ConfigManager) {
    private lateinit var mongoClient: MongoClient
    private lateinit var database: MongoDatabase
    private lateinit var collection: MongoCollection<Document>

    private val plugin = CaLevel.instance

    init {
        connect()
    }

    private fun connect() {
        val config: PluginConfig = configManager.pluginConfig

        try {
            mongoClient = MongoClients.create(config.mongoUri)
            database = mongoClient.getDatabase(config.mongoDatabase)
            collection = database.getCollection(config.mongoCollection)
            plugin.logger.info("成功连接到MongoDB数据库: ${config.mongoDatabase}.${config.mongoCollection}")
        } catch (e: Exception) {
            plugin.logger.severe("连接MongoDB失败: ${e.message}")
        }
    }

    fun getPlayerData(uuid: UUID): PlayerLevelData? {
        val doc = collection.find(Document("uuid", uuid.toString())).first() ?: return null
        return fromDocument(doc)
    }

    fun savePlayerData(data: PlayerLevelData) {
        val doc = toDocument(data)
        collection.replaceOne(Document("uuid", data.uuid.toString()), doc, com.mongodb.client.model.ReplaceOptions().upsert(true))
    }

    fun deletePlayerData(uuid: UUID) {
        collection.deleteOne(Document("uuid", uuid.toString()))
    }

    private fun toDocument(data: PlayerLevelData): Document {
        return Document().apply {
            append("uuid", data.uuid.toString())
            append("exp", data.exp)
            append("level", data.level)
            append("claimedRewards", data.claimedRewards)
        }
    }

    private fun fromDocument(doc: Document): PlayerLevelData {
        return PlayerLevelData(
            uuid = UUID.fromString(doc.getString("uuid")),
            exp = doc.getInteger("exp", 0),
            level = doc.getInteger("level", 0),
            claimedRewards = doc.getList("claimedRewards", Int::class.javaObjectType)?.toMutableList() ?: mutableListOf()
        )
    }

    fun close() {
        mongoClient.close()
    }
}