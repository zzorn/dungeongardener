package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase

/**
 * The specified parser should not match from here on, and the result of it is discarded
 */
class Not(val parser: Parser) : ParserBase() {

    constructor(text: String) : this(StringParser(text))
    constructor(vararg additional: Parser) : this(Sequence(*additional))
    constructor(vararg additional: String) : this(Sequence(*additional))

    override fun doParse(parserNode: ASTNode): Boolean {
        val success = !parser.parse(parserNode)
        if (success) {
            // Remove the parsed node
            parserNode.parent?.removeSubNode()
        }
        return success
    }
}