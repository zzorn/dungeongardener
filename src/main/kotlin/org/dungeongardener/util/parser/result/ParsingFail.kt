package org.dungeongardener.util.parser.result

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ASTNode

/**
 *
 */
class ParsingFail(val inputName: String = "input") : ParsingResult {

    var expected: Parser? = null
    var location: Int = 0
    var input: String? = null
    var leftRecursionError: Boolean = false

    override val success: Boolean = false

    override fun toString(): String {
        val errorLength = 40
        val s = input
        val e = expected
        if (s != null && e != null) {
            // TODO: Find row with error, print whole row with error
            val startIndex = Math.max(0, location - errorLength / 2)
            val endIndex = Math.min(location + errorLength / 2, s.length)
            val offset = if (startIndex < errorLength/2) location else errorLength/2

            val beforeError = s.substring(0, location)
            val row = beforeError.count { it == '\n' }
            val substring = s.substring(startIndex, endIndex)
            val column = if (row == 0) beforeError.length else {
                var index = beforeError.length - 1
                var count = 0
                while (index > 0 && beforeError[index] != '\n') {
                    index--
                    count++
                }
                count
            }

            if (leftRecursionError) {
                return "Left recursion when parsing '${e.name}' at row $row, column $column in '$inputName': \n$substring\n" + " ".repeat(offset) + "^\n"
            }
            else {
                return "Expected '${e.name}' at row $row, column $column in '$inputName': \n$substring\n" + " ".repeat(offset) + "^\n"
            }
        }
        else {
            return "Unreported error"
        }
    }

    fun update(failedNode: ASTNode) {
        expected = failedNode.parser
        location = failedNode.start
        input = failedNode.input
        leftRecursionError = false
    }

    fun leftRecursionError(rule: Parser, location: Int, input: String) {
        expected = rule
        this.location = location
        this.input = input
        leftRecursionError = true
    }

    fun clear() {
        expected = null
        location = 0
        input = null
        leftRecursionError = false
    }

    fun hasError(): Boolean = expected != null
}