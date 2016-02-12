package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import java.util.*

/**
 * Runs the sub activities in order.
 */
class OrderedActivity(override val name: String,
                      subActivities: List<Activity> = emptyList()) : ActivityBase(ArrayList(subActivities)) {

    override fun doEnter(character: Creature, callback: BackgroundCallback, backgroundState: Context, world: World): Boolean {
        var successes = false
        for (subActivity in subActivities) {
            val currentSuccess = subActivity.enter(character, callback, backgroundState, world)
            successes = successes || currentSuccess
        }

        return successes
    }
}