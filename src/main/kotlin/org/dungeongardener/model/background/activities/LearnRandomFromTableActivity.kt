package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.background.SkillTable
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.numberexpr.NumExpr

/**
 *
 */
class LearnRandomFromTableActivity(val expAmount: NumExpr, val rollCount: NumExpr, val skillTable: SkillTable) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {

        // Roll on skills table, add skills to character
        val rolls = rollCount.evaluate(context).toInt()
        for (i in 1..rolls) {
            // Determine skill to add exp to
            val skill = skillTable.rollRandomSkill(context)
            if (skill != null) {
                character.addSkillExp(skill, expAmount.evaluate(context))
            }
        }

        return true
    }
}