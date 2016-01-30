package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class Sequence(vararg val parsers: Parser) : ParserBase() {

    constructor(vararg texts: String) : this(*(texts.map { StringParser(it) }).toTypedArray())

    override fun doParse(parserNode: ParsingNode): Boolean {

        for (parser in parsers) {
            if (!parser.parse(parserNode)) {
                return false
            }
        }

        return true
    }
}
