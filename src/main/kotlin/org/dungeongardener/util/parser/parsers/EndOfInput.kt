package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class EndOfInput : ParserBase() {
    override fun doParse(parserNode: ParsingNode): Boolean {
        return parserNode.inputLeft() <= 0
    }
}