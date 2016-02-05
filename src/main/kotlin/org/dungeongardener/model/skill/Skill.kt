package org.dungeongardener.model.skill

import org.dungeongardener.model.creature.BasicStat

/**
 *
 */
data class Skill(val name: String,
                 val description: String,
                 val baseStat: BasicStat,
                 val difficulty: Double = 1.0) {
}