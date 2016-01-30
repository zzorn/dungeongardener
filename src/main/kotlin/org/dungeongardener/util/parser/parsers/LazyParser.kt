package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 * Delegates to a parser that will be assigned later.
 */
class LazyParser : Parser {
    lateinit var parser: Parser

    override fun parse(parent: ParsingNode): Boolean {
        return parser.parse(parent)
    }

    override var name: String
        get() = parser.name
        set(value) {
            parser.name = value
        }
}