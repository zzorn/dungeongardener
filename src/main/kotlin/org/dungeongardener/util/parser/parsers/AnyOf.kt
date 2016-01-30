package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class AnyOf(vararg val parsers: Parser) : ParserBase() {

    constructor(text: String) : this(StringParser(text))

    override fun doParse(parserNode: ParsingNode): Boolean {
        for (parser in parsers) {
            if (parser.parse(parserNode)) {
                return true
            }
        }
        return false
    }
}