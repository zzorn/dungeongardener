package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.model.skill.Skill
import org.dungeongardener.util.Context
import org.dungeongardener.util.numberexpr.NumExpr

/**
 *
 */
class LearnRandomFromListActivity(val expAmount: NumExpr, val rollCount: NumExpr, val skillList: List<Skill>) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {

        if (skillList.isNotEmpty()) {
            // Roll on skills lists, add skills to character
            val rolls = rollCount.evaluate(context).toInt()
            for (i in 1..rolls) {
                // Add exp to random skill
                val skill = context.random.nextElement(skillList)
                character.addSkillExp(skill, expAmount.evaluate(context))
            }
        }

        return true
    }
}