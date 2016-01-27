package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext

/**
 *
 */
class StringParser(val text: String) : Parser {
    override fun parse(context: ParsingContext): Boolean = context.consume(this, text)
}

val String.parser: StringParser
    get() = StringParser(this)