package org.dungeongardener.model.creature

/**
 * The basic character and creature stats.
 */
enum class BasicStat(val statName: String, val shortName: String, val basicCostToRaise: Double) {
    STR("Strength", "STR", 1.0),
    DEX("Dexterity", "DEX", 1.0),
    CON("Constitution", "CON", 1.0),
    INT("Intelligence", "INT", 1.0),
    WIL("Willpower", "WIL", 1.0),
    CAR("Carisma", "CAR", 1.0),
}