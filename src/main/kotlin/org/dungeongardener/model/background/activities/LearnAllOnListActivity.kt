package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 *
 */
class LearnAllOnListActivity(val expAmount: Expression, val skillNameList: List<String>) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        // Add exp to all skills
        for (skillName in skillNameList) {
            character.addSkillExp(world.getSkill(skillName), expAmount.evaluate(context))
        }

        return true
    }
}