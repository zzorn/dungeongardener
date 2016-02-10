package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import java.util.*

/**
 *
 */
abstract class ActivityBase() : Activity {

    val subActivities: MutableList<Activity> = ArrayList()

    fun addActivity(activity: Activity) {
        subActivities.add(activity)
    }

    override final fun enter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean {

        callback.enteredActivity(this)

        return try {doEnter(character, callback, backgroundState) }
            catch (e: InterruptActivityException) {
                if (e.targetActivity == name) {
                    // We should resume at this activity
                    return true
                }
                else {
                    // Resume higher up in the call hierarchy
                    throw e
                }
            }
    }

    protected abstract fun doEnter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean
}