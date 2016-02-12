package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 *
 */
interface Statement {
    fun process(context: Context)
}