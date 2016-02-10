package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context

/**
 * Runs the sub activities in order.
 */
class OrderedActivity(override val name: String) : ActivityBase() {

    override fun doEnter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean {
        for (subActivity in subActivities) {
            subActivity.enter(character, callback, backgroundState)
        }

        return true
    }
}