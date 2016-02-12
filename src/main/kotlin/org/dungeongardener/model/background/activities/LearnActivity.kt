package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.model.skill.Skill
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 *
 */
class LearnActivity(val expAmount: Expression, val skill: Skill) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {

        // Add exp to the skill
        character.addSkillExp(skill, expAmount.evaluate(context))

        return true
    }
}