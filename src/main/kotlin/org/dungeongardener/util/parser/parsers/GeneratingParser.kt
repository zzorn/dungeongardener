package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class GeneratingParser(val parser: Parser, val generate: (GeneratorContext) -> Any): Parser {

    override fun parse(context: ParsingContext): Boolean {
        if (parser.parse(context)) {
            context.pushResult(generate(context))
            return true
        }
        else return false
    }
}