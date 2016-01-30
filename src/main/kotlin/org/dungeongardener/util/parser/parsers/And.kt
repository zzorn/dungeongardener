package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * The specified parser should match from here on, but the result of it is discarded
 */
class And(val parser: Parser) : ParserBase() {

    constructor(text: String) : this(StringParser(text))

    override fun doParse(parserNode: ParsingNode): Boolean {
        val success = parser.parse(parserNode)
        if (success) {
            // Remove the parsed node
            parserNode.parent?.removeSubNode()
        }
        return success
    }
}