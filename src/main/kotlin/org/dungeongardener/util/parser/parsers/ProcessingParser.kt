package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * Can manipulate results based on the text matched by the specified parser.
 * Can access and remove previously pushed results and push additional results.
 * Only applied to fully parsing inputs.
 */
class ProcessingParser(val parser: Parser, val process: (GeneratorContext) -> Unit): ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {
        parserNode.resultProcessor = process
        return parser.parse(parserNode)
    }
}