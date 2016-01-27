package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 * Wraps a parser that will be assigned later.
 */
class LazyParser : Parser {
    lateinit var parser: Parser

    override fun parse(context: ParsingContext): Boolean {
        return parser.parse(context)
    }
}