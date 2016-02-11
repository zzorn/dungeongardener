package org.dungeongardener.model.background.activities

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.util.Context
import org.dungeongardener.util.boolexpr.BoolExpr

/**
 * Only do the nested activity if the conditions are met
 */
class ConditionalActivity(val activityOnSuccess: Activity,
                          val activityOnFailure: Activity?,
                          val condition: BoolExpr,
                          override val name: String = "Condition ${condition}") : Activity {

    override fun enter(character: Creature, callback: BackgroundCallback, context: Context): Boolean {

        val success = condition.evaluate(context)

        if (success) {
            activityOnSuccess.enter(character, callback, context)
        }
        else {
            if (activityOnFailure != null) activityOnFailure.enter(character, callback, context)
            else return false
        }

        return true
    }
}