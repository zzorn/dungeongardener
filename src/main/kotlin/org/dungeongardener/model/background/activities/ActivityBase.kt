package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.background.InterruptActivityException
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import java.util.*

/**
 *
 */
abstract class ActivityBase(val subActivities: MutableList<Activity> = ArrayList()) : Activity {

    fun addActivity(activity: Activity) {
        subActivities.add(activity)
    }

    override final fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        callback.enteredActivity(this)

        return try {doEnter(character, callback, context, world) }
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

    protected abstract fun doEnter(character: Creature, callback: BackgroundCallback, backgroundState: Context, world: World): Boolean

    override fun toString(): String {
        return name
    }
}