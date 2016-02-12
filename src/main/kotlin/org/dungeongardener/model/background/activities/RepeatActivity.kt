package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 * Repeats an activity the specified number of times, or until the activity fails
 */
class RepeatActivity(val repeatCount: Expression, val activity: Activity) : SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {
        val repeats = repeatCount.evaluate<Double>(context).toInt()

        for(i in 1 .. repeats) {
            val success = activity.enter(character, callback, context)
            if (!success) return false
        }

        return true
    }
}