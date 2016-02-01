package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ASTNode

/**
 * Matches everything, doesn't consume anything.
 */
class AutoMatch : ParserBase() {
    override fun doParse(parserNode: ASTNode): Boolean {
        return true
    }
}