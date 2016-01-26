package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class Sequence(val first: Parser, val second: Parser) : Parser {
    override fun parse(context: ParsingContext): Boolean {
        if (first.parse(context)) {
            if (!second.parse(context)) {
                context.popParsePosition()
                return false
            }
            else return true
        }
        else return false
    }
}
