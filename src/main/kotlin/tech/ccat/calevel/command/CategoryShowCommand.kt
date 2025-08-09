package tech.ccat.calevel.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.ccat.calevel.CaLevel
import tech.ccat.calevel.api.CaLevelAPI
import tech.ccat.calevel.model.ExpCategory
import tech.ccat.calevel.model.ExpProviderData
import tech.ccat.calevel.util.MessageFormatter

class CategoryShowCommand : AbstractCommand(
    name = "category",
    usage = "/level category <类别>",
    minArgs = 1,
    playerOnly = true
) {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        val player = sender as Player
        val categoryName = args[1].uppercase()

        // 获取API实例
        val api = CaLevel.instance.server.servicesManager.getRegistration(CaLevelAPI::class.java)?.provider ?: run {
            sender.sendMessage(MessageFormatter.format("api-not-available"))
            return true
        }

        // 解析经验类别
        val category = try {
            ExpCategory.valueOf(categoryName)
        } catch (_: IllegalArgumentException) {
            sender.sendMessage(MessageFormatter.format("invalid-category", categoryName))
            return true
        }

        // 获取类别下的经验数据
        val dataList = api.getExperienceDataByCategory(player, category)

        // 显示类别信息
        showCategoryInfo(player, category, dataList)

        return true
    }

    private fun showCategoryInfo(player: Player, category: ExpCategory, dataList: List<ExpProviderData>) {
        // 显示类别标题
        player.sendMessage(MessageFormatter.format("category-header", category.displayName))

        if (dataList.isEmpty()) {
            // 没有提供器的情况
            player.sendMessage(MessageFormatter.format("no-providers-in-category"))
            return
        }

        // 显示每个提供器的数据
        dataList.forEach { data ->
            // 显示提供器基本信息
            player.sendMessage(MessageFormatter.format(
                "category-provider-entry",
                data.name,
                data.currentExp,
                data.totalExp,
                data.progress
            ))

            // 显示提示信息（如果有）
            if (data.hint.isNotEmpty()) {
                player.sendMessage(MessageFormatter.format("category-provider-hint", data.hint))
            }
        }

        // 计算类别总计
        val totalCurrent = dataList.sumOf { it.currentExp }
        val totalMax = dataList.sumOf { it.totalExp }
        val totalProgress = if (totalMax > 0) String.format("%.2f", totalCurrent.toDouble() / totalMax * 100) else "0.00"

        // 显示类别总计
        player.sendMessage(MessageFormatter.format(
            "category-total",
            totalCurrent,
            totalMax,
            totalProgress
        ))
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return if (args.size == 2) {
            ExpCategory.entries.map { it.name }
        } else {
            emptyList()
        }
    }
}