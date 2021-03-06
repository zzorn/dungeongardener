package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase

/**
 *
 */
class SequenceParser(vararg var parsers: Parser) : ParserBase() {

    constructor(vararg texts: String) : this(*(texts.map { StringParser(it) }).toTypedArray())

    override fun doParse(parserNode: ASTNode): Boolean {

        for (parser in parsers) {
            if (parser == null || !parser.parse(parserNode)) {
                return false
            }
        }

        return true
    }


}
