package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ASTNode

/**
 * Generates result that is pushed on the result stack based on the text matched by the specified parser.
 * Can also access and remove previously pushed results and push additional results.
 * Only applied to fully parsing inputs.
 */
class GeneratingParser(val parser: Parser, val generate: (GeneratorContext) -> Any): ParserBase() {

    override fun doParse(parserNode: ASTNode): Boolean {

        // Store the result generator with this node
        parserNode.resultGenerator = generate

        return parser.parse(parserNode)
    }
}