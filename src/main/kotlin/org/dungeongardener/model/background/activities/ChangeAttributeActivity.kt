package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Attribute
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression
import java.util.*

/**
 * Changes the specified attributes
 */
class ChangeAttributeActivity(override val name: String,
                              val change: Expression,
                              val affectedStats: EnumSet<Attribute>) : SimpleActivity() {
    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {
        for (affectedStat in affectedStats) {
            character.basicAttributes.changeAttribute(affectedStat, change.evaluate(context))
        }

        return true
    }
}