package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import java.util.*

/**
 * Pick one of the sub-activities
 */
class ChoiceActivity(override val name: String,
                     subActivities: List<Activity> = emptyList()) : ActivityBase(ArrayList(subActivities)) {

    override fun doEnter(character: Creature, callback: BackgroundCallback, backgroundState: Context, world: World): Boolean {

        val availableActivities = ArrayList(subActivities)

        // Allow picking alternatives until we run out of them
        var success = false
        while (availableActivities.isNotEmpty() && !success) {
            val selectedActivity = callback.selectActivity(availableActivities)
            success = selectedActivity.enter(character, callback, backgroundState, world)
            if (!success) {
                availableActivities.remove(selectedActivity)
            }
        }

        return success
    }
}