package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ASTNode

/**
 * Input should end here
 */
class EndOfInput : ParserBase() {
    override fun doParse(parserNode: ASTNode): Boolean {
        return parserNode.inputLeft() <= 0
    }
}