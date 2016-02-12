package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 *
 */
data class AssignmentStatement(val variableName: String, val expression: Expression) : Statement {

    override fun process(context: Context) {
        context.setReference(variableName, expression)
    }

}