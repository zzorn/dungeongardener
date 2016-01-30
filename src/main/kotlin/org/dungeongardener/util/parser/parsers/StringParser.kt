package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class StringParser(val text: String) : ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {
        return parserNode.attemptToConsumeText(text)
    }

    override var name: String
        get() = /*super.name + " " + */ text
        set(value) {
            super.name = value
        }
}

val String.parser: StringParser
    get() = StringParser(this)

operator fun String.unaryMinus(): StringParser = StringParser(this)
