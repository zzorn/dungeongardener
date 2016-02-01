package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.result.ParseSuccess
import org.dungeongardener.util.parser.result.ParsingFail
import java.util.*

/**
 * Used for storing abstract syntax tree data while parsing input.
 */
class ASTNode(val input: String,
              val parser: Parser? = null,
              val start: Int = 0,
              var ownLength: Int = 0,
              val errorMessage: ParsingFail = ParsingFail(),
              val parent: ASTNode? = null,
              val packRatCache: MutableMap<PackRatKey, PackRatEntry> = HashMap()) {

    data class PackRatKey(val parser: Parser, val startPos: Int)
    data class PackRatEntry(var astNode: ASTNode?, var endPos: Int, var leftRecursionDetected: Boolean = false)

    var resultGenerator: ((GeneratorContext) -> Any)? = null
    var resultProcessor: ((GeneratorContext) -> Unit)? = null

    private var subNodes: Deque<ASTNode> = LinkedList()
    private var firstGeneratedResultIndex = 0
    private var lastGeneratedResultIndex = 0

    val end: Int
        get() = Math.max(start + ownLength, if (subNodes.isNotEmpty()) subNodes.last.end else (start + ownLength))

    val matchedText: String
        get() = input.substring(start, end)

    fun addSubNode(subNode: ASTNode) {
        subNodes.addLast(subNode)
    }

    fun addSubNode(parser: Parser): ASTNode {
        val subNode = ASTNode(input, parser, end, 0, errorMessage, this, packRatCache)
        addSubNode(subNode)
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

    fun getResultsGeneratedInThisNode(allResults: List<Any>): List<Any> {
        if (lastGeneratedResultIndex <= firstGeneratedResultIndex) return Collections.emptyList()
        else return allResults.subList(firstGeneratedResultIndex, lastGeneratedResultIndex)
    }

    protected fun generateResults(context: GeneratorContext) {

        context.currentNode = this
        firstGeneratedResultIndex = context.results.size

        // Generate results for sub-nodes
        for (subNode in subNodes) {
            subNode.generateResults(context)
        }

        lastGeneratedResultIndex = context.results.size

        // Generate result for this node
        val generator = resultGenerator
        if (generator != null) {
            context.text = matchedText
            context.push(generator(context))
        }

        lastGeneratedResultIndex = context.results.size

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

    fun removeCurrentNodeResults(results: LinkedList<Any>) {
        var count = lastGeneratedResultIndex - firstGeneratedResultIndex
        while(count > 0) {
            results.removeAt(firstGeneratedResultIndex)
            count--
        }
    }


}