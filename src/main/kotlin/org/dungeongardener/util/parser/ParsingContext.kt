package org.dungeongardener.util.parser

import java.util.*

/**
 *
 */
class ParsingContext(val input: CharSequence) {

    private data class Node(var position: Int, var matchedText: String = "", var pushedResults: Int = 0)
    private var currentPosition: Int = 0
    private val parsingStack: Deque<Node> = LinkedList()
    private val results: Deque<Any> = LinkedList()

    init {
        parsingStack.push(Node(currentPosition))
    }

    fun getResults(): List<Any> = ArrayList(results)

    fun pushResult(result: Any) {
        parsingStack.peek().pushedResults++
        results.push(result)
    }

    fun popResult(): Any {
        parsingStack.peek().pushedResults--
        return results.pop()
    }

    fun pushParsePosition(matchedText: String) {
        parsingStack.push(Node(currentPosition, matchedText))
    }

    fun popParsePosition() {
        val node = parsingStack.pop()
        currentPosition = node.position
        for (i in 0 .. node.pushedResults) results.pop()
    }

    fun inputLeft(): Int = input.length - currentPosition

    fun startsWith(text: String): Boolean {
        if (inputLeft() < text.length) return false

        for (i in 0 .. text.length - 1) {
            if (text[i] != input[currentPosition + i]) return false
        }

        return true
    }

    fun consume(text: String): Boolean {
        if (startsWith(text)) {
            pushParsePosition(text)
            currentPosition += text.length
            return true
        }
        else {
            return false
        }
    }



}