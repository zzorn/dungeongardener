package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context

/**
 * Something that can be done as part of a background occupation
 */
interface Activity {

    val name: String

    /**
     * @return true if entrance to the activity was successful, false if the activity could not be entered and it had to
     * be immediately scrapped before time was spent on it (possible to choose another activity instead of it).
     */
    fun enter(character: Creature, callback: BackgroundCallback, backgroundState: Context): Boolean

}