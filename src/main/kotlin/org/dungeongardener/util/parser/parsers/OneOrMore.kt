package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class OneOrMore(val parser: Parser): ParserBase() {

    constructor(text: String) : this(StringParser(text))

    override fun doParse(parserNode: ParsingNode): Boolean {
        if (!parser.parse(parserNode)) {
            return false
        }
        else {
            while (parser.parse(parserNode)) {}
            return true
        }

    }
}