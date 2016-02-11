package org.dungeongardener.model.background

import org.dungeongardener.model.background.activities.Activity
import org.dungeongardener.model.skill.Skill

/**
 *
 */
interface BackgroundCallback {

    fun note(message: String)

    /**
     * Asks if it is ok to pay the specified sum
     */
    fun pay(message: String, gold: Double): Boolean

    fun skillExp(skill: Skill, exp: Double)

    /**
     * Select one of the specified activities
     */
    fun selectActivity(activities: List<Activity>): Activity

    fun enteredActivity(activity: Activity)

}