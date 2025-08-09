package tech.ccat.calevel.model

import org.bukkit.Material

data class ExpProviderData(
    val name: String,
    val icon: Material,
    val currentExp: Int,
    val totalExp: Int,
    val hint: String
) {
    val progress: Double
        get() = if (totalExp > 0) {
            (currentExp.toDouble() / totalExp * 100).let {
                String.format("%.2f", it).toDouble()
            }
        } else {
            0.0
        }
}