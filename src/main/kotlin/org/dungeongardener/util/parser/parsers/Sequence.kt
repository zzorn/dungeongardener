package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class Sequence(vararg val parsers: Parser) : Parser {
    override fun parse(context: ParsingContext): Boolean {

        context.addSubNodeAndRecurse(this)

        for (i in 0 .. parsers.size - 1) {
            val parser = parsers.get(i)

            if (!parser.parse(context)) {

                // Pop what we parsed
                context.popSubNodes(i - 1)

                return false
            }
        }

        context.moveUp()

        return true
    }
}
