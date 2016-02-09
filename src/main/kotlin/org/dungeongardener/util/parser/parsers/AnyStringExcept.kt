package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.ParserBase

/**
 * Matches any string (including empty string) up until the exceptString.
 */
class AnyStringExcept(val exceptString: String, val ignoreCase: Boolean = false) : ParserBase() {

    init {
        if (exceptString.length <= 0) throw IllegalArgumentException("exceptString must not be empty")
    }

    override fun doParse(parserNode: ASTNode): Boolean {
        while (true) {
            val nextInput = parserNode.nextInputChar()
            if (nextInput == null) return true
            if (nextInput == exceptString[0] && parserNode.inputContinuesWith(exceptString, ignoreCase)) {
                return true
            }
            parserNode.consumeInput(1)
        }
    }

    override var name: String
        get() = "AnyStringExcept(\"$exceptString\")"
        set(value) {
            super.name = value
        }
}