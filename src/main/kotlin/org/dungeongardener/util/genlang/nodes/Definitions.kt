package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 *
 */
interface Definitions {

    val imports: List<Import>

    fun process(context: Context)

}

