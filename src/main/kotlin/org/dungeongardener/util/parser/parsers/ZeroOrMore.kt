package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * Matches the specified parser zero or more times.
 */
class ZeroOrMore(val parser: Parser): ParserBase() {

    constructor(text: String) : this(StringParser(text))
    constructor(first: Parser, vararg additional: Parser) : this(Sequence(first, *additional))
    constructor(first: String, vararg additional: String) : this(Sequence(first, *additional))

    override fun doParse(parserNode: ParsingNode): Boolean {

        while (parser.parse(parserNode)) {}

        return true
    }
}