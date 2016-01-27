package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class EndOfInput : Parser {
    override fun parse(context: ParsingContext): Boolean {
        return context.inputLeft() <= 0
    }
}