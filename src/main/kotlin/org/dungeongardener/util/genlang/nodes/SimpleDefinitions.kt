package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 *
 */
class SimpleDefinitions(override val imports: List<Import>, val statements: List<Statement>) : Definitions {

    /**
     * This assumes all imports are already imported.
     */
    override fun process(context: Context, importContext: ImportContext) {
        for (statement in statements) {
            statement.process(context)
        }
    }

}

