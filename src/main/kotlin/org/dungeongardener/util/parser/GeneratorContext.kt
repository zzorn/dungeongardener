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

    fun push(result: Any) = results.addLast(result)
    fun <T> pop(): T = results.removeLast() as T

}