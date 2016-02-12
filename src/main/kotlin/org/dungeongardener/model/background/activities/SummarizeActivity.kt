package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context

/**
 * Used to summarize some activity when generating printed materials.
 */
class SummarizeActivity(override val name: String, val description: String, val activity: Activity) : Activity {
    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {
        return activity.enter(character, callback, context)
    }
}