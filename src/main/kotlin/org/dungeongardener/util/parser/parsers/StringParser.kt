package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ASTNode

/**
 * Matches the specified string, succeeds if the string is found at this location, fails if not.
 * Requires exact case match by default, has flag to ignore case.
 */
class StringParser(val text: String, val ignoreCase: Boolean = false) : ParserBase() {

    override fun doParse(parserNode: ASTNode): Boolean {
        return parserNode.attemptToConsumeText(text, ignoreCase)
    }

    override var name: String
        get() = /*super.name + " " + */ text
        set(value) {
            super.name = value
        }
}

val String.parser: StringParser
    get() = StringParser(this)

operator fun String.unaryPlus(): StringParser = StringParser(this)
