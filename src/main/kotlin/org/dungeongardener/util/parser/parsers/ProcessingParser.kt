package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class ProcessingParser(val parser: Parser, val process: (GeneratorContext) -> Unit): ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {
        parserNode.resultProcessor = process
        return parser.parse(parserNode)
    }
}