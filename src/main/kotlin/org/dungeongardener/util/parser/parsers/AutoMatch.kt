package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * Matches everything, doesn't consume anything.
 */
class AutoMatch : ParserBase() {
    override fun doParse(parserNode: ParsingNode): Boolean {
        return true
    }
}