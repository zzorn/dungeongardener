package org.dungeongardener.model.creature

import org.dungeongardener.model.TimeOfDay
import org.dungeongardener.model.TimeOfYear
import org.dungeongardener.model.terrain.TerrainType
import org.dungeongardener.util.numberexpr.NumExpr
import java.util.*

/**
 * Describes a type of creature
 */
data class CreatureType (
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
        val weigth_kg: NumExpr,
        val length_m: NumExpr,
        val distribution: MutableMap<TerrainType, Commonality> = HashMap(),
        val dailyActivity: MutableMap<TimeOfDay, Double> = HashMap(),
        val yearlyActivity: MutableMap<TimeOfYear, Double> = HashMap()
) {
    init{
        family.creatureTypes.add(this)
    }



}