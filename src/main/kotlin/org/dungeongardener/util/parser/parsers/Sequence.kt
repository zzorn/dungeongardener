package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ASTNode

/**
 *
 */
class Sequence(vararg val parsers: Parser) : ParserBase() {

    constructor(vararg texts: String) : this(*(texts.map { StringParser(it) }).toTypedArray())

    override fun doParse(parserNode: ASTNode): Boolean {

        for (parser in parsers) {
            if (!parser.parse(parserNode)) {
                return false
            }
        }

        return true
    }
}
