package org.dungeongardener.util.parser

import org.dungeongardener.util.Context

/**
 * Passed to a function, used to get the parameters of the function as well as the current context, and the name of the function if needed.
 */
data class FunctionContext<T>(val functionName: String, val context: Context, val parameters: List<T>) {

    val parameterCount: Int = parameters.size

    fun p(index: Int): T = parameters[index] as T
    fun <P>pAs(index: Int): P = parameters[index] as P


    val a: T get() = parameters[0]
    val b: T get() = parameters[1]
    val c: T get() = parameters[2]
    val d: T get() = parameters[3]
    val e: T get() = parameters[4]
    val f: T get() = parameters[5]
    val g: T get() = parameters[6]
    val h: T get() = parameters[7]

    val x: T get() = parameters[0]
    val y: T get() = parameters[1]
    val z: T get() = parameters[2]

    val p1: T get() = parameters[0]
    val p2: T get() = parameters[1]
    val p3: T get() = parameters[2]
    val p4: T get() = parameters[3]
    val p5: T get() = parameters[4]
    val p6: T get() = parameters[5]
    val p7: T get() = parameters[6]
    val p8: T get() = parameters[7]
    val p9: T get() = parameters[8]
    val p10: T get() = parameters[9]
}