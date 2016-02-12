package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 * Node of a parsed genlang program.
 */
interface Expression {

    fun <T>evaluate(context: Context): T

}