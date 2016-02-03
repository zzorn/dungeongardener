package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase

/**
 * Matches the specified parser zero or more times.
 */
class ZeroOrMore(val parser: Parser): ParserBase() {

    constructor(text: String) : this(StringParser(text))
    constructor(vararg additional: Parser) : this(Sequence(*additional))
    constructor(vararg additional: String) : this(Sequence(*additional))

    override fun doParse(parserNode: ASTNode): Boolean {

        while (parser.parse(parserNode)) {}

        return true
    }
}