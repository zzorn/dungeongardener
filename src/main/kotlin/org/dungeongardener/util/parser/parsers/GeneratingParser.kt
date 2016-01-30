package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class GeneratingParser(val parser: Parser, val generate: (GeneratorContext) -> Any): ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {

        // Store the result generator with this node
        parserNode.resultGenerator = generate

        return parser.parse(parserNode)
    }
}