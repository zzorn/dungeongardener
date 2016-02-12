package org.dungeongardener.model.creature

import org.dungeongardener.model.TimeOfDay
import org.dungeongardener.model.TimeOfYear
import org.dungeongardener.model.terrain.TerrainType
import org.dungeongardener.util.genlang.nodes.ConstantExpr
import org.dungeongardener.util.genlang.nodes.Expression
import org.dungeongardener.util.numberexpr.DiceExpr
import java.util.*

/**
 * Describes a type of creature
 */
data class CreatureType (
        val name: String,
        val summary: String = "",
        val appearance: String = "",
        val behaviour: String = "",
        val abilities: String = "",
        val backgroundLore: String = "",
        val numberWhenMoving: Expression = ConstantExpr(1.0),
        val numberInLair: Expression = ConstantExpr(1.0),
        val family: CreatureFamily? = null,
        val stats: StatBlock = StatBlock(DiceExpr(6, 3), DiceExpr(6, 3), DiceExpr(6, 3), DiceExpr(6, 3), DiceExpr(6, 3)),
        val weigth_kg: Expression  = ConstantExpr(10.0),
        val length_m: Expression = ConstantExpr(1.0),
        val distribution: MutableMap<TerrainType, Commonality> = HashMap(),
        val dailyActivity: MutableMap<TimeOfDay, Double> = HashMap(),
        val yearlyActivity: MutableMap<TimeOfYear, Double> = HashMap()
) {
    init{
        family?.creatureTypes?.add(this)
    }



}