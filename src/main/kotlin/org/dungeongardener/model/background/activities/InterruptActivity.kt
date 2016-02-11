package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.background.InterruptActivityException
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context

/**
 *
 */
class InterruptActivity(val targetActivityName: String): SimpleActivity() {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {
        throw InterruptActivityException(targetActivityName)
    }
}