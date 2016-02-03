package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase

/**
 * Applies the first of the specified parsers that matches, fails if none of them matches.
 */
class AnyOf(vararg val parsers: Parser) : ParserBase() {

    constructor(vararg texts: String) : this(*(texts.map { StringParser(it) }).toTypedArray())
    constructor(texts: Collection<String>) : this(*(texts.map { StringParser(it) }).toTypedArray())

    override fun doParse(parserNode: ASTNode): Boolean {
        for (parser in parsers) {
            if (parser.parse(parserNode)) {
                return true
            }
        }
        return false
    }
}