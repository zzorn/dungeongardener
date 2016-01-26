package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class Optional(val parser: Parser) : Parser {
    override fun parse(context: ParsingContext): Boolean {
        parser.parse(context)
        return true
    }
}