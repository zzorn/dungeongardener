package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * The specified parser should not match from here on, and the result of it is discarded
 */
class Not(val parser: Parser) : ParserBase() {

    constructor(text: String) : this(StringParser(text))

    override fun doParse(parserNode: ParsingNode): Boolean {
        val success = !parser.parse(parserNode)
        if (success) {
            // Remove the parsed node
            parserNode.parent?.removeSubNode()
        }
        return success
    }
}