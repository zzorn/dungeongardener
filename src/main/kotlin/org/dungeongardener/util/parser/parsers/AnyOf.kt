package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class AnyOf(vararg val parsers: Parser) : Parser {
    override fun parse(context: ParsingContext): Boolean {
        for (parser in parsers) {
            if (parser.parse(context)) return true
        }

        return false
    }
}