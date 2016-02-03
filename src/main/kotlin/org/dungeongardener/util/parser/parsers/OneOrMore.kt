package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase

/**
 * Matches the specified parser one or more times.
 */
class OneOrMore(val parser: Parser): ParserBase() {

    constructor(text: String) : this(StringParser(text))
    constructor(vararg additional: Parser) : this(Sequence(*additional))
    constructor(vararg additional: String) : this(Sequence(*additional))

    override fun doParse(parserNode: ASTNode): Boolean {
        if (!parser.parse(parserNode)) {
            return false
        }
        else {
            while (parser.parse(parserNode)) {}
            return true
        }

    }
}