package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * Matches the specified parser one or more times.
 */
class OneOrMore(val parser: Parser): ParserBase() {

    constructor(text: String) : this(StringParser(text))
    constructor(first: Parser, vararg additional: Parser) : this(Sequence(first, *additional))
    constructor(first: String, vararg additional: String) : this(Sequence(first, *additional))

    override fun doParse(parserNode: ParsingNode): Boolean {
        if (!parser.parse(parserNode)) {
            return false
        }
        else {
            while (parser.parse(parserNode)) {}
            return true
        }

    }
}