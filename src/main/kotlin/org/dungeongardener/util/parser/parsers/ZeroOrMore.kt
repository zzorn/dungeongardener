package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class ZeroOrMore(val parser: Parser): ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {

        while (parser.parse(parserNode)) {}

        return true
    }
}