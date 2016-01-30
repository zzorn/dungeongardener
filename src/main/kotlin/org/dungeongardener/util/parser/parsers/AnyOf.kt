package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class AnyOf(vararg val parsers: Parser) : ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {
        for (parser in parsers) {
            if (parser.parse(parserNode)) {
                return true
            }
        }
        return false
    }
}