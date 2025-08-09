package tech.ccat.calevel

import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import tech.ccat.calevel.api.CaLevelAPI
import tech.ccat.calevel.api.CaLevelAPIImpl
import tech.ccat.calevel.command.ClaimRewardCommand
import tech.ccat.calevel.command.CommandManager
import tech.ccat.calevel.command.LevelShowCommand
import tech.ccat.calevel.command.SelfCheckCommand
import tech.ccat.calevel.config.ConfigManager
import tech.ccat.calevel.dao.PlayerLevelDao
import tech.ccat.calevel.listener.*
import tech.ccat.calevel.model.PlayerLevelData
import tech.ccat.calevel.provider.LevelRewardStatProvider
import tech.ccat.calevel.service.*
import tech.ccat.kstats.api.KStatsAPI

class CaLevel : JavaPlugin(){
    // 依赖注入
    lateinit var configManager: ConfigManager
    lateinit var playerCache: PlayerLevelCache
    lateinit var playerDao: PlayerLevelDao
    lateinit var expManager: ExperienceManager
    lateinit var rewardManager: RewardManager
    lateinit var commandManager: CommandManager

    // API 实现实例
    private lateinit var caLevelAPI: CaLevelAPIImpl

    // KStats API 引用
    private var kstatsAPI: KStatsAPI? = null

    companion object {
        lateinit var instance: CaLevel
            private set

        /**
         * 获取KStatsAPI服务
         */
        fun getKStatsAPI(): KStatsAPI? {
            return instance.kstatsAPI
        }
    }

    override fun onEnable() {
        instance = this
        logger.info("CaLevel 等级系统正在启动...")

        // 1. 初始化核心模块
        configManager = ConfigManager().apply { setup() }
        playerCache = PlayerLevelCache()
        playerDao = PlayerLevelDao(configManager)
        expManager = ExperienceManager(playerCache, configManager)
        rewardManager = RewardManager(playerCache, configManager)
        commandManager = CommandManager()

        // 2. 注册事件监听器
        registerListeners()

        // 3. 初始化命令系统
        registerCommands()

        // 4. 注册API服务
        caLevelAPI = CaLevelAPIImpl(this)
        server.servicesManager.register(
            CaLevelAPI::class.java,
            caLevelAPI,
            this,
            ServicePriority.Normal
        )

        // 5. 连接到KStats（如果存在）
        connectToKStats()

        logger.info("CaLevel 等级系统已成功启用")

        if(!Bukkit.getOnlinePlayers().isEmpty()){
            midInitPlayerData()
        }
    }

    override fun onDisable() {
        // 1. 保存所有玩家数据
        playerCache.saveAllPlayers(playerDao)

        // 2. 断开与KStats的连接
        disconnectFromKStats()

        // 3. 关闭数据库连接
        playerDao.close()

        // 4. 取消注册服务
        server.servicesManager.unregisterAll(this)

        logger.info("CaLevel 等级系统已成功禁用")
    }

    fun getKStatsAPI(): KStatsAPI? {
        return kstatsAPI
    }

    //中途重载的玩家数据加载
    private fun midInitPlayerData(){
        Bukkit.getOnlinePlayers().forEach{
            playerCache.addPlayerData(it.uniqueId, playerDao.getPlayerData(it.uniqueId) ?: PlayerLevelData(it.uniqueId))
            expManager.updatePlayerExperience(it)
        }
    }

    private fun connectToKStats() {
        // 尝试获取KStats服务
        kstatsAPI = server.servicesManager.getRegistration(KStatsAPI::class.java)?.provider

        // 注册属性提供器到KStats
        if (kstatsAPI != null) {
            LevelRewardStatProvider.registerToKStats()
            logger.info("已成功注册到KStats属性系统")
        } else {
            logger.warning("未找到KStats服务，属性奖励功能不可用")
        }
    }

    private fun disconnectFromKStats() {
        // 取消注册KStats属性提供器
        LevelRewardStatProvider.unregisterFromKStats()
        kstatsAPI = null
    }

    private fun registerListeners() {
        val manager = server.pluginManager
        manager.registerEvents(PlayerLoginListener(this), this)
        manager.registerEvents(PlayerQuitListener(this), this)
        manager.registerEvents(ExpUpdateListener(this), this)
        manager.registerEvents(LevelUpListener(this), this)
        manager.registerEvents(PlayerDataSaveListener(this), this)
    }

    private fun registerCommands() {
        commandManager.register(SelfCheckCommand())
        commandManager.register(LevelShowCommand())
        commandManager.register(ClaimRewardCommand())
        getCommand("level")?.setExecutor(commandManager)
        getCommand("level")?.tabCompleter = commandManager
    }

}