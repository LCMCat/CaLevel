package tech.ccat.calevel.model

import org.bukkit.Material

enum class ExpCategory(
    val displayName: String,
    val icon: Material
) {
    CORE("核心项目", Material.DIAMOND),
    EVENT("事件项目", Material.CLOCK),
    INSTANCE("副本项目", Material.NETHER_STAR),
    CONQUEST("征服项目", Material.IRON_SWORD),
    SKILL("技能项目", Material.EXPERIENCE_BOTTLE),
    MISCELLANEOUS("杂项", Material.CHEST);
}