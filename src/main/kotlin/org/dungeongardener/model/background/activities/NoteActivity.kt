package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context

/**
 * Just makes a note in the history
 */
class NoteActivity(override val name: String) : ActivityBase() {
    override fun doEnter(character: Creature, callback: BackgroundCallback, backgroundState: Context, world: World): Boolean {
        callback.note(name)
        return true
    }
}