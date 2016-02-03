package org.dungeongardener.util.parser

import java.util.*

/**
 * Used when generating output for a parsed input
 * @param Parsing node that we are generating content for.
 */
class GeneratorContext(val printDebugInfo: Boolean = false) {

    lateinit var currentNode: ASTNode

    /**
     * Total text covered by current node
     */
    var text: String = ""
       // get() = currentNode.matchedText

    /**
     * Results collected so far
     */
    var results: LinkedList<Any> = LinkedList()

    /**
     * Add given result to the end of the results.
     */
    fun push(result: Any) {
        results.addLast(result)
        if (printDebugInfo) println("Pushing ${result.javaClass.simpleName}: '$result'" + resultStats())
    }

    /**
     * Remove and return the last pushed result.
     */
    fun <T> pop(indexBack: Int = 0): T  {
        val value = results.removeAt(results.size - indexBack - 1) as T
        if (printDebugInfo) println("Popping '$value'" + resultStats())
        return value
    }

    /**
     * Results collected from the current node, assuming that no results generated before this node were popped
     * (in which case the behaviour can be undefined).
     * Note that the returned list is backed by the result list, so if the result list changes, the returned list also changes.
     */
    val currentNodeResults: List<Any>
        get() = currentNode.getResultsGeneratedInThisNode(results)

    /**
     * Removes and returns any results generated by the current node, assuming that no results generated outside the current node were popped
     * (in which case the behaviour can be undefined).
     * Returns a new array list with the results.
     */
    fun <T> popCurrentNodeResults(): ArrayList<T> {
        val poppedResults = ArrayList(currentNodeResults)
        removeCurrentNodeResults()
        if (printDebugInfo) println("Popping results for current node: '${poppedResults.joinToString()}' + resultStats()")
        return poppedResults as ArrayList<T>
    }

    /**
     * Removes any results generated by the current node, assuming that no results generated outside the current node were popped
     * (in which case the behaviour can be undefined).
     */
    fun removeCurrentNodeResults() {
        currentNode.removeCurrentNodeResults(results)
        if (printDebugInfo) println("  Removing results for current node" + resultStats())
    }

    private fun resultStats(): String {
        return "\n  Result stack (size ${results.size}): '" + results.joinToString() + "'"
    }
}