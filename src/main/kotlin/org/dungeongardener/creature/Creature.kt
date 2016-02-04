package org.dungeongardener.creature

import org.dungeongardener.util.numberexpr.NumExpr
import java.util.*

/**
 * Describes a type of creature
 */
// TODO: Allow using another creature as template, the template creature may or may not be abstract...
data class Creature(
        val name: String,
        val summary: String,
        val appearance: String,
        val behaviour: String,
        val abilities: String,
        val backgroundLore: String,
        val numberWhenMoving: NumExpr,
        val numberInLair: NumExpr,
        val family: CreatureFamily,
        val stats: StatBlock,
        val distribution: MutableMap<TerrainType, Commonality> = HashMap(),
        val dailyActivity: MutableMap<TimeOfDay, Double> = HashMap(),
        val yearlyActivity: MutableMap<TimeOfYear, Double> = HashMap()
) {
    init{
        family.creatures.add(this)
    }



}