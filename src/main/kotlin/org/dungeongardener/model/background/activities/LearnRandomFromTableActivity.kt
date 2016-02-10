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

    override fun enter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean {

        val exp = expAmount.evaluate(backgroundState)
        val rolls = rollCount.evaluate(backgroundState)

        // TODO: Roll on skills table, add skills to character

        return true
    }
}