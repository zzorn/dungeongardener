package org.dungeongardener.model.background.activities

import org.dungeongardener.model.Table
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.model.skill.Skill
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.ConstantExpr
import org.dungeongardener.util.genlang.nodes.Expression

/**
 *
 */
class LearnRandomFromTableActivity(val expAmount: Expression,
                                   val rollCount: Expression,
                                   val tableModifier: Expression = ConstantExpr(0.0),
                                   val skillTable: Table<out Skill>) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {

        // Roll on skills table, add skills to character
        val rolls = rollCount.evaluate<Double>(context).toInt()
        for (i in 1..rolls) {
            // Calculate modifier
            val modifier = tableModifier.evaluate<Double>(context)

            // Determine skill to add exp to
            val skill = skillTable.randomize(context, modifier)
            if (skill != null) {
                character.addSkillExp(skill, expAmount.evaluate(context))
            }
        }

        return true
    }
}