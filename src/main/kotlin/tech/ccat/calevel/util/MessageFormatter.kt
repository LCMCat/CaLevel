package tech.ccat.calevel.util

import tech.ccat.calevel.CaLevel

object MessageFormatter {
    fun format(key: String, vararg args: Any): String {
        return CaLevel.instance.configManager.messageConfig.getMessage(key, *args)
    }
}