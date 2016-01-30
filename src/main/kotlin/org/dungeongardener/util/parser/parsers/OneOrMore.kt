package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class OneOrMore(val parser: Parser): ParserBase() {
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