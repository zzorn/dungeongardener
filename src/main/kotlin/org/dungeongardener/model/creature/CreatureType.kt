package org.dungeongardener.model.creature

import org.dungeongardener.model.TimeOfDay
import org.dungeongardener.model.TimeOfYear
import org.dungeongardener.model.terrain.TerrainType
import org.dungeongardener.util.genlang.nodes.Expression
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
        val numberWhenMoving: Expression,
        val numberInLair: Expression,
        val family: CreatureFamily,
        val stats: StatBlock,
        val weigth_kg: Expression,
        val length_m: Expression,
        val distribution: MutableMap<TerrainType, Commonality> = HashMap(),
        val dailyActivity: MutableMap<TimeOfDay, Double> = HashMap(),
        val yearlyActivity: MutableMap<TimeOfYear, Double> = HashMap()
) {
    init{
        family.creatureTypes.add(this)
    }



}