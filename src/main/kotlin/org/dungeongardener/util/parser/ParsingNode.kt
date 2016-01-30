package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.result.ParseSuccess
import org.dungeongardener.util.parser.result.ParsingFail
import java.util.*

/**
 * Used for storing syntax tree data while parsing input.
 */
class ParsingNode(val input: String,
                  val parser: Parser? = null,
                  val start: Int = 0,
                  var ownLength: Int = 0,
                  val errorMessage: ParsingFail = ParsingFail(),
                  val parent: ParsingNode? = null) {

    var resultGenerator: ((GeneratorContext) -> Any)? = null
    var resultProcessor: ((GeneratorContext) -> Unit)? = null

    private var subNodes: Deque<ParsingNode> = LinkedList()

    val end: Int
        get() = Math.max(start + ownLength, if (subNodes.isNotEmpty()) subNodes.last.end else start)

    val matchedText: String
        get() = input.substring(start, end)

    fun addSubNode(parser: Parser): ParsingNode {
        val subNode = ParsingNode(input, parser, end, 0, errorMessage, this)
        subNodes.addLast(subNode)
        return subNode
    }

    fun removeSubNode() {
        subNodes.removeLast()
    }

    fun inputLeft(): Int = input.length - end

    fun nextInputChar(forwardOffset: Int = 0): Char? {
        if (inputLeft() < forwardOffset + 1) return null
        else return input[end + forwardOffset]
    }

    fun inputContinuesWith(text: String, ignoreCase: Boolean = false): Boolean {
        if (inputLeft() < text.length) return false

        for (i in 0 .. text.length - 1) {
            if (!ignoreCase) {
                if (text[i] != input[end + i]) return false
            }
            else {
                if (text[i].toUpperCase() != input[end + i].toUpperCase()) return false
            }
        }

        return true
    }

    fun attemptToConsumeChar(attemptedChar: Char): Boolean {
        if (nextInputChar() === attemptedChar) {
            consumeInput(1)
            return true
        }
        else return false
    }

    fun attemptToConsumeText(attemptedText: String, ignoreCase: Boolean = false): Boolean {
        if (inputContinuesWith(attemptedText, ignoreCase)) {
            consumeInput(attemptedText.length)
            return true
        }
        else return false
    }

    fun consumeInput(numberOfCharacters: Int) {
        ownLength = numberOfCharacters
    }

    fun generateResults(): ParseSuccess {
        val generatorContext = GeneratorContext()
        generateResults(generatorContext)
        return ParseSuccess(generatorContext.results)
    }

    protected fun generateResults(context: GeneratorContext) {
        // Generate results for sub-nodes
        for (subNode in subNodes) {
            subNode.generateResults(context)
        }

        // Generate result for this node
        val generator = resultGenerator
        if (generator != null) {
            context.text = matchedText
            context.push(generator(context))
        }

        // Process result for this node
        val processor = resultProcessor
        if (processor != null) {
            context.text = matchedText
            processor(context)
        }
    }

    override fun toString(): String {
        val s = StringBuilder()
        toString(s, 0)
        return s.toString()
    }

    private fun toString(s: StringBuilder, indent: Int) {
        for (i in 1..indent) s.append("  ")
        if (ownLength > 0) {
            s.append("\"")
            s.append(matchedText)
            s.append("\"")
        }
        else {
            s.append(parser?.name?.capitalize() ?: "Root")
        }

        s.append("\n")

        if (subNodes.isNotEmpty()) {
            for (subNode in subNodes) {
                subNode.toString(s, indent + 1)
            }
        }
    }


}