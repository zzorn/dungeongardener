package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class ZeroOrMoreCharParser(val acceptedCharacters: CharSequence) : ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {

        var offset = 0
        while (nextCharIsValid(parserNode, offset)) {
            offset++
        }

        if (offset > 0) parserNode.consumeInput(offset)

        return true
    }

    private fun nextCharIsValid(parserNode: ParsingNode, offset: Int = 0): Boolean {
        val nextChar = parserNode.nextInputChar(offset)
        return nextChar != null && acceptedCharacters.contains(nextChar)
    }
}