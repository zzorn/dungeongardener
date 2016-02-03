package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.Multiplicity.ONE_OR_MORE
import org.dungeongardener.util.parser.Multiplicity.ZERO_OR_MORE
import org.dungeongardener.util.parser.parsers.*
import org.dungeongardener.util.parser.result.ParsingResult
import java.io.File

/**
 * Base class for parsers, provides utility functions.
 */
abstract class ParserHelper<T> {

    abstract val parser: Parser

    /**
     * Parse the content of the input file and return a parse success or failure object.
     */
    fun parse(inputFile: File, debugOutput: Boolean = false) : ParsingResult = parser.parse(inputFile.readText(), inputFile.name, debugOutput = debugOutput)

    /**
     * Parse the input text and return a parse success or failure object.
     */
    fun parse(input: String, inputName: String = "input", debugOutput: Boolean = false) : ParsingResult = parser.parse(input, inputName, debugOutput)

    /**
     * Parse the input text and return the first result.
     * @throws ParsingError if there is no results, or if the parse failed.
     */
    fun <T>parseFirst(input: String, inputName: String = "input", debugOutput: Boolean = false) : T = parser.parseFirst(input, inputName, debugOutput)

    /**
     * Parse the text of the input file and return the first result.
     * @throws ParsingError if there is no results, or if the parse failed.
     */
    fun <T>parseFirst(inputFile: File, debugOutput: Boolean = false) : T = parser.parseFirst(inputFile, debugOutput = debugOutput)

    protected fun <P : Parser> parser(name: String? = null, parserFunc: () -> P): P {
        val parser = parserFunc()

        if (name != null) parser.name = name

        return parser
    }

    protected val lazy: LazyParser
        get() = LazyParser()

    protected fun any(vararg parsers: Parser): AnyOf = AnyOf(*parsers)
    protected fun any(vararg parsers: String): AnyOf = AnyOf(*parsers)
    protected fun any(parsers: Collection<String>): AnyOf = AnyOf(parsers)

    protected fun opt(vararg parsers: Parser): Optional = Optional(*parsers)
    protected fun opt(vararg parsers: String): Optional = Optional(*parsers)

    protected fun oneOrMore(vararg parsers: Parser): OneOrMore = OneOrMore(*parsers)
    protected fun oneOrMore(vararg parsers: String): OneOrMore = OneOrMore(*parsers)

    protected fun zeroOrMore(vararg parsers: Parser): ZeroOrMore = ZeroOrMore(*parsers)
    protected fun zeroOrMore(vararg parsers: String): ZeroOrMore = ZeroOrMore(*parsers)

    protected fun not(vararg parsers: Parser): Not = Not(*parsers)
    protected fun not(vararg parsers: String): Not = Not(*parsers)

    protected fun and(vararg parsers: Parser): And = And(*parsers)
    protected fun and(vararg parsers: String): And = And(*parsers)

    protected val anyChar: CharParser
        get() = CharParser("").anyExcept()

    protected fun char(chars: String): CharParser = CharParser(chars)
    protected fun oneOrMoreChars(chars: String): CharParser = CharParser(chars, ONE_OR_MORE)
    protected fun zeroOrMoreChars(chars: String): CharParser = CharParser(chars, ZERO_OR_MORE)

    protected fun char(vararg chars: CharRange): CharParser = CharParser(*chars)
    protected fun oneOrMoreChars(vararg chars: CharRange): CharParser = CharParser(ONE_OR_MORE, *chars)
    protected fun zeroOrMoreChars(vararg chars: CharRange): CharParser = CharParser(ZERO_OR_MORE, *chars)

    protected val autoMatch: AutoMatch
        get() = AutoMatch()

    protected val endOfInput: EndOfInput
        get() = EndOfInput()

    protected operator fun String.unaryPlus(): StringParser = StringParser(this)
    protected operator fun String.plus(parser: Parser): Sequence = Sequence(StringParser(this), parser)

}