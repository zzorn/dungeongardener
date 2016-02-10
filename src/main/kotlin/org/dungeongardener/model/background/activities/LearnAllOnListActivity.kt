package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.model.skill.Skill
import org.dungeongardener.util.Context
import org.dungeongardener.util.numberexpr.NumExpr

/**
 *
 */
class LearnAllOnListActivity(val expAmount: NumExpr, val skillList: List<Skill>) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean {

        val exp = expAmount.evaluate(backgroundState)

        // TODO: Add exp to all skills

        return true
    }
}