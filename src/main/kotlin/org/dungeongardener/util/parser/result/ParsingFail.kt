package org.dungeongardener.util.parser.result

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class ParsingFail(val inputName: String = "input") : ParsingResult {

    var expected: Parser? = null
    var location: Int = 0
    var input: String? = null

    override val success: Boolean = false

    override fun toString(): String {
        val errorLength = 40
        val s = input
        val e = expected
        if (s != null && e != null) {
            val startIndex = Math.max(0, location - errorLength / 2)
            val endIndex = Math.min(location + errorLength / 2, s.length)
            val substring = s.substring(startIndex, endIndex)
            val offset = if (startIndex < errorLength/2) location else errorLength/2

            val beforeError = s.substring(0, location)
            val row = beforeError.count { it == '\n' }
            val column = if (row == 0) beforeError.length else {
                var index = beforeError.length - 1
                var count = 0
                while (index > 0 && beforeError[index] != '\n') {
                    index--
                    count++
                }
                count
            }
            return "Expected '${e.name}' at row $row, column $column in '$inputName': \n$substring\n" + " ".repeat(offset) + "^\n"
        }
        else {
            return "Unreported error"
        }
    }

    fun update(failedNode: ParsingNode) {
        expected = failedNode.parser
        location = failedNode.start
        input = failedNode.input
    }

    fun clear() {
        expected = null
        location = 0
        input = null
    }

    fun hasError(): Boolean = expected != null
}