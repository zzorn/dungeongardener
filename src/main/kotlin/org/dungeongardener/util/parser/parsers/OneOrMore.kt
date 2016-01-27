package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class OneOrMore(val parser: Parser): Parser {
    override fun parse(context: ParsingContext): Boolean {

        context.addSubNodeAndRecurse(this)

        if (!parser.parse(context)) {
            context.popUp()
            return false
        }
        else {
            while (parser.parse(context)) {}
            context.moveUp()
            return true
        }

    }
}