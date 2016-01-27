package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class ZeroOrMore(val parser: Parser): Parser {
    override fun parse(context: ParsingContext): Boolean {
        context.addSubNodeAndRecurse(this)

        while (parser.parse(context)) {}

        context.moveUp()

        return true
    }
}