package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class Choice(val first: Parser, val second: Parser) : Parser {
    override fun parse(context: ParsingContext): Boolean {
        return first.parse(context) || second.parse(context)
    }
}