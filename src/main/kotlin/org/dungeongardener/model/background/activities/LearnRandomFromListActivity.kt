package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 *
 */
class LearnRandomFromListActivity(val expAmount: Expression, val rollCount: Expression, val skillList: List<String>) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        if (skillList.isNotEmpty()) {
            // Roll on skills lists, add skills to character
            val rolls = rollCount.evaluate<Double>(context).toInt()
            for (i in 1..rolls) {
                // Add exp to random skill
                val skill = context.random.nextElement(skillList)
                character.addSkillExp(world.getSkill(skill), expAmount.evaluate(context))
            }
        }

        return true
    }
}