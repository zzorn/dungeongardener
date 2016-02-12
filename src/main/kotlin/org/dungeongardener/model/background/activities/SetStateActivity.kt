package org.dungeongardener.model.background.activities

import org.dungeongardener.model.World
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression

/**
 * Change some state in the context.
 */
class SetStateActivity(val variable: String, val value: Expression) : SimpleActivity() {
    override fun enter(character: Creature, callback: BackgroundCallback, context: Context, world: World): Boolean {

        context.setReference(variable, value.evaluate(context))

        return true
    }
}