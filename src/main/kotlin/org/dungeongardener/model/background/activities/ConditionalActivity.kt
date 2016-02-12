package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 * Only do the nested activity if the conditions are met
 */
class ConditionalActivity(val activityOnSuccess: Activity,
                          val activityOnFailure: Activity?,
                          val condition: Expression,
                          override val name: String = "Condition ${condition}") : Activity {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        val success = condition.evaluate<Boolean>(context)

        if (success) {
            activityOnSuccess.enter(character, callback, context, world)
        }
        else {
            if (activityOnFailure != null) activityOnFailure.enter(character, callback, context, world)
            else return false
        }

        return true
    }
}