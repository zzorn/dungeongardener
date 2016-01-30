package org.dungeongardener.util.parser

import java.util.*

/**
 * Used when generating output for a parsed input
 */
class GeneratorContext {

    /**
     * Total text covered by current node
     */
    var text: String = ""

    /**
     * Results collected so far
     */
    var results: LinkedList<Any> = LinkedList()

    /**
     * Add given result to the end of the results.
     */
    fun push(result: Any) = results.addLast(result)

    /**
     * Remove and return the last pushed result.
     */
    fun <T> pop(): T = results.removeLast() as T

}