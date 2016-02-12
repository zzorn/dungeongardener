package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.ParserBase
import java.util.*

class DynamicAnyOfStrings(val stringsToParse: Collection<String> = ArrayList()): ParserBase() {
    override fun doParse(parserNode: ASTNode): Boolean {
        for (stringToParser in stringsToParse) {
            if (parserNode.attemptToConsumeText(stringToParser)) {
                return true
            }
        }
        return false
    }
}