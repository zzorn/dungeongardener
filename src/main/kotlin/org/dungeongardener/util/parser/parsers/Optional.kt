package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase

/**
 * Matches the specified parser if it matches, but succeeds even if it does not match.
 * (Equivalent to zeroOrOne)
 */
class Optional(val parser: Parser) : ParserBase() {

    constructor(text: String) : this(StringParser(text))
    constructor(vararg parsers: Parser) : this(Sequence(*parsers))
    constructor(vararg parsers: String) : this(Sequence(*parsers))

    override fun doParse(parserNode: ASTNode): Boolean {
        parser.parse(parserNode)
        return true
    }
}