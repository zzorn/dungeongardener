package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class ProcessingParser(val parser: Parser, val process: (GeneratorContext) -> Unit): Parser {

    override fun parse(context: ParsingContext): Boolean {
        if (parser.parse(context)) {
            process(context)
            return true
        }
        else return false
    }
}