package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingNode

/**
 *
 */
class StringParser(val text: String) : ParserBase() {

    override fun doParse(parserNode: ParsingNode): Boolean {
        return parserNode.attemptToConsumeText(text)
    }

}

val String.parser: StringParser
    get() = StringParser(this)