package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.Multiplicity
import org.dungeongardener.util.parser.ParserBase
import org.dungeongardener.util.parser.ParsingNode

/**
 * Some of the specified characters with the specified multiplicity (zero or one, one, zero or more, one or more - defaults to one)
 */
class CharParser(val acceptedCharacters: CharSequence, val multiplicity: Multiplicity = Multiplicity.ONE, val negated: Boolean = false) : ParserBase() {

    constructor(character: Char, multiplicity: Multiplicity = Multiplicity.ONE) : this(character.toString(), multiplicity)
    constructor(multiplicity: Multiplicity, vararg characterRanges: CharRange) : this(characterRanges.joinToString("") { it.joinToString("") }, multiplicity )
    constructor(vararg characterRanges: CharRange) : this(characterRanges.joinToString("") { it.joinToString("") })

    /**
     * A parser that matches any characters except the ones that this parser matches
     */
    fun anyExcept(): CharParser {
        return CharParser(acceptedCharacters, multiplicity, !negated)
    }

    override fun doParse(parserNode: ParsingNode): Boolean {

        return when (multiplicity) {
            Multiplicity.OPTIONAL -> {
                consumeValidChar(parserNode)
                true
            }
            Multiplicity.ONE -> {
                consumeValidChar(parserNode)
            }
            Multiplicity.ZERO_OR_MORE -> {
                consumeValidChars(parserNode)
                true
            }
            Multiplicity.ONE_OR_MORE -> {
                consumeValidChars(parserNode)
            }
        }

    }

    private fun consumeValidChar(parserNode: ParsingNode): Boolean {
        if (nextCharIsValid(parserNode)) {
            parserNode.consumeInput(1)
            return true
        }
        else return false
    }

    private fun consumeValidChars(parserNode: ParsingNode): Boolean {
        var offset = 0
        while (nextCharIsValid(parserNode, offset)) {
            offset++
        }

        if (offset > 0) {
            parserNode.consumeInput(offset)
            return true
        }
        else return false
    }

    private fun nextCharIsValid(parserNode: ParsingNode, offset: Int = 0): Boolean {
        val nextChar = parserNode.nextInputChar(offset)
        return nextChar != null &&
                (if (negated) !acceptedCharacters.contains(nextChar)
                 else          acceptedCharacters.contains(nextChar))
    }

}