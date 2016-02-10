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

    override fun enter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean {

        val exp = expAmount.evaluate(backgroundState)
        val rolls = rollCount.evaluate(backgroundState)

        // TODO: Roll on skills lists, add skills to character

        return true
    }
}