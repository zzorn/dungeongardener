package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 *
 */
class LearnActivity(val expAmount: Expression, val skillName: String) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        // Add exp to the skill
        val skill = world.getSkill(skillName)
        val skillExp = expAmount.evaluate<Double>(context)
        character.addSkillExp(skill, skillExp)
        callback.skillExp(skill, skillExp)

        return true
    }
}