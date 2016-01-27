package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class CharParser(val acceptedCharacters: CharSequence) : Parser{
    override fun parse(context: ParsingContext): Boolean {

        if (nextCharIsValid(context)) {
            context.consume(this, 1)
            return true
        }
        else return false
    }

    private fun nextCharIsValid(context: ParsingContext, offset: Int = 0): Boolean {
        val nextChar = context.getNextChar(offset)
        return nextChar != null && acceptedCharacters.contains(nextChar)
    }
}