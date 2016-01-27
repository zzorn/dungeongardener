package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class ZeroOrMoreCharParser(val acceptedCharacters: CharSequence) : Parser{
    override fun parse(context: ParsingContext): Boolean {

        var offset = 0
        while (nextCharIsValid(context, offset)) {
            offset++
        }

        if (offset > 0) context.consume(this, offset)

        return true
    }

    private fun nextCharIsValid(context: ParsingContext, offset: Int = 0): Boolean {
        val nextChar = context.getNextChar(offset)
        return nextChar != null && acceptedCharacters.contains(nextChar)
    }
}