package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Attribute
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 * Changes one random attribute
 */
class ChangeRandomAttributeActivity(override val name: String,
                              val change: Expression) : SimpleActivity() {
    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        val attribute = context.random.nextElement(Attribute.values())

        character.basicAttributes.changeAttribute(attribute, change.evaluate(context))

        return true
    }
}