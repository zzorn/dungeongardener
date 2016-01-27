package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.parsers.Parser
import org.dungeongardener.util.parser.result.ParseSuccess
import org.dungeongardener.util.parser.result.ParsingResult
import java.util.*

/**
 *
 */
class ParsingContext(val input: CharSequence) : GeneratorContext {

    private class Node(val parser: Parser?, val startPos: Int, var endPos: Int = startPos, val parent: Node? = null) {
        private var subNodes: Deque<Node> = LinkedList()
        var resultsAdded: Int = 0

        fun addSubNode(parser: Parser, startPos: Int, endPos: Int): Node {
            val subNode = Node(parser, startPos, endPos, this)
            subNodes.push(subNode)
            updateEnd(endPos)
            return subNode
        }

        private fun updateEnd(endPos: Int) {
            this.endPos = endPos
            parent?.updateEnd(endPos)
        }

        fun matchedText(context: ParsingContext): String {
            println(toDebugString(context))
            return context.input.substring(startPos, endPos)
        }

        fun popNode(context: ParsingContext): Int {
            for (subNode in subNodes) subNode.popNode(context)
            for (i in 1 .. resultsAdded) context.results.pop()
            updateEnd(startPos)
            return startPos
        }

        fun popSubNode(context: ParsingContext): Int {
            endPos = subNodes.pop().popNode(context)
            return endPos
        }

        fun toDebugString(context: ParsingContext): String {
            val s  = StringBuilder()
            toDebugString(context, s)
            return s.toString()
        }

        fun toDebugString(context: ParsingContext, s: StringBuilder) {
            s.append((parser?.javaClass?.simpleName).orEmpty())
            s.append("( ")
            if (subNodes.isNotEmpty()) {
                for(subNode in subNodes) s.append(subNode.toDebugString(context, s))
            }
            else {
                s.append("'")
                s.append(context.input.subSequence(startPos, endPos))
                s.append("'")
            }
            s.append(" )")
        }
    }

    private var currentPosition: Int = 0
    private var currentNode: Node = Node(null, 0, 0)
    private val results: Deque<Any> = LinkedList()

    override val matched: String
        get() = currentNode.matchedText(this)

    fun getResult(): ParsingResult {
        val r = ArrayList(results)
        r.reverse()
        return ParseSuccess(r)
    }

    override fun pushResult(result: Any) {
        results.push(result)
    }

    override fun popResult(): Any {
        return results.pop()
    }

    fun addSubNode(parser: Parser, startPos: Int, endPos: Int) {
        val node = currentNode.addSubNode(parser, startPos, endPos)
        currentPosition = node.endPos
    }

    fun addSubNodeAndRecurse(parser: Parser) {
        currentNode = currentNode.addSubNode(parser, currentPosition, currentPosition)
    }

    fun popUp() {
        currentNode.popNode(this)
        moveUp()
    }

    fun moveUp() {
        currentNode = currentNode.parent ?: throw IllegalStateException("current node has no parent")
    }

    fun popSubNodes(numberOfPopsToDo: Int) {
        for (i in 1..numberOfPopsToDo) popSubNode()
    }

    fun popSubNode() {
        currentPosition = currentNode.popSubNode(this)
    }

    fun inputLeft(): Int = input.length - currentPosition

    fun startsWith(text: String): Boolean {
        if (inputLeft() < text.length) return false

        for (i in 0 .. text.length - 1) {
            if (text[i] != input[currentPosition + i]) return false
        }

        return true
    }

    fun consume(parser: Parser, text: String): Boolean {
        if (startsWith(text)) {
            addSubNode(parser, currentPosition, currentPosition + text.length)
            return true
        }
        else {
            return false
        }
    }

    fun consume(parser: Parser, numberOfChars: Int) {
        addSubNode(parser, currentPosition, currentPosition + numberOfChars)
    }

    fun getNextChar(forwardOffset: Int = 0): Char? {
        if (inputLeft() < forwardOffset + 1) return null
        else return input[currentPosition + forwardOffset]
    }

}