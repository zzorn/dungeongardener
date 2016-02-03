package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser

/**
 * Delegates to a parser that will be assigned later.
 */
class LazyParser : Parser {
    lateinit var parser: Parser

    override fun parse(parent: ASTNode): Boolean {
        return parser.parse(parent)
    }

    override var name: String
        get() = parser.name
        set(value) {
            parser.name = value
        }
}