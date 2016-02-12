package org.dungeongardener.model.background.activities

import org.dungeongardener.model.Table
import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.ConstantExpr
import org.dungeongardener.util.genlang.nodes.Expression

/**
 * Selects an activity from a table of activities.
 */
class TableActivity(val activityTable: Table<Activity>, val modifier: Expression = ConstantExpr(0.0)) : SimpleActivity() {

    override val name: String
        get() = activityTable.name

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {
        val randomActivity = activityTable.randomize(context, modifier.evaluate(context))
        if (randomActivity != null) randomActivity.enter(character, callback, context)
        return true
    }
}