package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class CharParser(val acceptedCharacters: CharSequence) : ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {
        val nextChar = parserNode.nextInputChar()
        if (nextChar != null && acceptedCharacters.contains(nextChar)) {
            parserNode.consumeInput(1)
            return true
        }
        else return false
    }
}