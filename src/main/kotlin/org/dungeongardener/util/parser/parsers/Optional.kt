package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class Optional(val parser: Parser) : ParserBase() {
    override fun doParse(parserNode: ParsingNode): Boolean {
        parser.parse(parserNode)
        return true
    }
}