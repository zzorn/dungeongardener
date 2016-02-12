package org.dungeongardener.util.parser

import org.dungeongardener.util.genlang.nodes.InfixExpr
import org.dungeongardener.util.parser.parsers.*
import org.flowutils.Symbol
import java.util.*

/**
 * Provides even more commonly used language constructs.
 *
 * Adds support for operators with precedence, builtin functions, comment types, and various primitive and collection parsing.
 */
abstract class LanguageBase<T>(override val extension: Symbol) : Language<T>() {

    constructor(extension: String) : this(Symbol.get(extension))

    private class DynamicAnyOf(val parsers: Collection<Parser> = ArrayList()): ParserBase() {
        override fun doParse(parserNode: ASTNode): Boolean {
            for (parser in parsers) {
                if (parser.parse(parserNode)) {
                    return true
                }
            }
            return false
        }
    }

    private val commentParsers = ArrayList<Parser>()
    private val commentParser = DynamicAnyOf(commentParsers)

    /**
     * Adds a comment starting with the startSequence and ending in a newline or end of input.
     */
    fun addEndOfLineComment(startSequence: String, ignoreCase: Boolean = false, name: String = "endOfLineComment") {
        commentParsers.add(
                SequenceParser(
                        StringParser(startSequence, ignoreCase),
                        zeroOrMoreCharsExcept("\n"),
                        any(+"\n", endOfInput)
                ).named(name)
        )
    }

    /**
     * Adds a new type of block comment.
     */
    fun addBlockComment(startSequence: String, endSequence: String, ignoreCase: Boolean = false, name: String = "blockComment") {
        commentParsers.add(
                SequenceParser(
                        StringParser(startSequence, ignoreCase),
                        AnyStringExcept(endSequence, ignoreCase),
                        StringParser(endSequence, ignoreCase)
                ).named(name)
        )
    }

    /**
     * Non-commented whitespace parser (by default sees spaces, newlines and tabs as whitespace)
     */
    open val plainWhitespace = oneOrMoreChars(" \n\t")

    /**
     * Parses whitespace, including whitespace characters and comments
     */
    override val whitespace = parser("whitespace") {
        zeroOrMore(any(plainWhitespace, commentParser))
    }

    /**
     * Parses English letters in upper or lower case (a..z and A..Z) by default
     */
    open val letter = CharParser('a'..'z', 'A'..'Z').named("letter")

    /**
     * Parses English letters or number or underscore in upper or lower case (a..z and A..Z) by default
     */
    open val identifierPart = CharParser('a'..'z', 'A'..'Z', '0'..'9', '_'..'_').named("identifierPart")

    /**
     * Parses a typical programming language identifier, starting with a letter or underscore and followed by zero or more letters, underscores, and numbers.
     * Pushes the identifier to the result stack as a string.
     */
    open val identifier = SequenceParser(
            CharParser('a'..'z', 'A'..'Z', '_'..'_'),
            CharParser(Multiplicity.ZERO_OR_MORE, 'a'..'z', 'A'..'Z', '0'..'9', '_'..'_'),
            not(identifierPart)
    ).named("identifier").generatesMatchedText()

    /**
     * A keyword that is not followed by any additional identifier characters, but can be followed by whitespace
     */
    fun keyword(keyword: String, ignoreCase: Boolean = false) = StringParser(keyword, ignoreCase) + not(identifierPart) + ws

    /**
     * Parses a string quoted with " characters, and pushes the content of the string to the result stack as a string.
     * Does not consume any trailing whitespace.
     * Allows escaping of newlines \n, tabs \t, quotes \" and backslashes \\
     */
    open val quotedString = quotedStringWithEscaping()

    /**
     * Parses a string quoted with " characters, and pushes the content of the string to the result stack as a string.
     * Does not consume any trailing whitespace.
     * Does not support any escape sequences for newline and the like.
     */
    open val quotedStringWithoutEscaping = SequenceParser(+"\"", zeroOrMoreCharsExcept("\"").generatesMatchedText(), +"\"").named("quotedString")

    /**
     * Creates a parser for quoted strings with support for escaping newlines, tabs, quote characters and the escape character.
     */
    fun quotedStringWithEscaping(quoteChar: Char = '\"',
                                 escapeChar: Char = '\\',
                                 allowNewlineEscape: Boolean = true,
                                 allowTabEscape: Boolean = true,
                                 name: String = "quotedString") = SequenceParser(
            char("" + quoteChar),
            zeroOrMore(
                    any(
                            oneOrMoreCharsExcept("" + quoteChar + escapeChar),
                            SequenceParser(+(""+escapeChar), anyChar)
                    )
            ).generates {
                var result: String = it.text
                if (allowNewlineEscape) result = result.replace(escapeChar + "n", "\n")
                if (allowTabEscape)     result = result.replace(escapeChar + "t", "\t")
                result = result.replace("" + escapeChar + quoteChar, "" + quoteChar)
                result = result.replace("" + escapeChar + escapeChar, "" + escapeChar)
                result
            },
            char("" + quoteChar)
    ).named(name)

    /**
     * Parses a positive integer.  Note that the integer can start with leading zeroes, which are discarded when parsing it.
     * Pushes the parsed integer to the result stack.
     * Does not consume any trailing whitespace.
     */
    val positiveInteger = oneOrMoreChars('0'..'9').generates { it.text.toInt() }.named("positiveInteger")

    /**
     * Parses a positive or negative integer.  Allows whitespace between a minus sign and the integer.
     * Does not consume any trailing whitespace.
     * Pushes the parsed integer to the result stack.
     */
    val integer = SequenceParser(
            any((+"-").generates { -1 }, autoMatch.generates { 1 }),
            ws,
            oneOrMoreChars('0'..'9').generates { it.text.toInt() * it.pop<Int>() }
    ).named("integer")

    /**
     * Parses a positive or negative double.  Allows whitespace between a minus sign and the number.
     * Allows scientific E notation for the exponent (no whitespace is allowed between the number and the exponent)
     * Does not consume any trailing whitespace.
     * Pushes the parsed integer to the result stack.
     */
    val double = SequenceParser(
            any((+"-").generates { -1.0 }, autoMatch.generates { 1.0 }),
            ws,
            SequenceParser(
                    oneOrMoreChars('0'..'9'),
                    opt(+".", oneOrMoreChars('0'..'9')),
                    opt(char("eE"), opt(+"-"), oneOrMoreChars('0'..'9'))
            ).generates { it.text.toDouble() * it.pop<Double>() }
    ).named("double")

    /**
     * Parses a list of zero or more elements with some start and end markers and separators.
     */
    fun listParser(elementParser: Parser,
                   nodeGenerator: (List<Any>) -> Any = { it },
                   startMarker: Parser = +"(",
                   endMarker: Parser = +")",
                   elementSeparator: Parser = +",",
                   allowWhitespace: Boolean = true,
                   parseTrailingWhitespace: Boolean = true): Parser {

        return SequenceParser(
                startMarker,
                if (allowWhitespace) ws else autoMatch,
                opt(
                        elementParser,
                        if (allowWhitespace) ws else autoMatch,
                        ZeroOrMore(
                                elementSeparator,
                                if (allowWhitespace) ws else autoMatch,
                                elementParser,
                                if (allowWhitespace) ws else autoMatch
                        )
                ),
                endMarker,
                if (parseTrailingWhitespace) ws else autoMatch
        ).generates { nodeGenerator(it.popCurrentNodeResults<Any>()) }
    }

    /**
     * Parses a list of two or more elements with some separators.
     */
    fun listParserWithoutStartEndMarkers(elementParser: Parser,
                   nodeGenerator: (List<Any>) -> Any = { it },
                   elementSeparator: Parser = +",",
                   allowWhitespace: Boolean = true): Parser {

        return SequenceParser(
                elementParser,
                if (allowWhitespace) ws else autoMatch,
                OneOrMore(
                        elementSeparator,
                        if (allowWhitespace) ws else autoMatch,
                        elementParser,
                        if (allowWhitespace) ws else autoMatch
                )
        ).generates { nodeGenerator(it.popCurrentNodeResults<Any>()) }
    }

    /**
     * Parses a map of zero or more elements with some start, end, element and key-value separators.
     */
    fun mapParser(keyParser: Parser,
                  valueParser: Parser,
                  nodeGenerator: (LinkedHashMap<Any, Any>) -> Any = { it },
                  startMarker: String = "{",
                  endMarker: String = "}",
                  elementSeparator: Parser = +",",
                  keyValueSeparator: Parser = +":",
                  allowWhitespace: Boolean = true,
                  parseTrailingWhitespace: Boolean = true): Parser {

        val keyValueParser = SequenceParser(
                keyParser,
                if (allowWhitespace) ws else autoMatch,
                keyValueSeparator,
                if (allowWhitespace) ws else autoMatch,
                valueParser,
                if (allowWhitespace) ws else autoMatch
        ).generates { Pair<Any, Any>(it.pop(1), it.pop()) }

        return SequenceParser(
                +startMarker,
                if (allowWhitespace) ws else autoMatch,
                opt(
                        keyValueParser,
                        ZeroOrMore(
                                elementSeparator,
                                if (allowWhitespace) ws else autoMatch,
                                keyValueParser
                        )
                ),
                +endMarker,
                if (parseTrailingWhitespace) ws else autoMatch
        ).generates {
            val map = LinkedHashMap<Any, Any>()
            map.putAll(it.popCurrentNodeResults<Pair<Any, Any>>())
            nodeGenerator(map)
        }
    }

    /**
     * Parses a map of one or more elements with some element and key-value separators.
     */
    fun mapParserWithoutStartEndMarkers(keyParser: Parser,
                  valueParser: Parser,
                  nodeGenerator: (LinkedHashMap<Any, Any>) -> Any = { it },
                  elementSeparator: Parser = +",",
                  keyValueSeparator: Parser = +":",
                  allowWhitespace: Boolean = true): Parser {

        val keyValueParser = SequenceParser(
                keyParser,
                if (allowWhitespace) ws else autoMatch,
                keyValueSeparator,
                if (allowWhitespace) ws else autoMatch,
                valueParser,
                if (allowWhitespace) ws else autoMatch
        ).generates { Pair<Any, Any>(it.pop(1), it.pop()) }

        return SequenceParser(
                keyValueParser,
                zeroOrMore(
                        elementSeparator,
                        if (allowWhitespace) ws else autoMatch,
                        keyValueParser
                )
        ).generates {
            val map = LinkedHashMap<Any, Any>()
            map.putAll(it.popCurrentNodeResults<Pair<Any, Any>>())
            nodeGenerator(map)
        }
    }

    /**
     * Creates a parser that handles operators with different precedence.
     * @param atomParser the smallest component, used as the inputs for the highest precedence operations.
     * @param operators one or more mappings from the operator sign to the generator function for the operator,
     *                  starting with the highest precedence operator and ending with the lowest one.
     *                  If no operators are given, the atomParser is returned as is.
     */
    fun <T> operatorParser(atomParser: Parser, vararg operators: Pair<String, (T, T) -> Any>): Parser {
        var parser: Parser = atomParser
        for (operator in operators) {
            val currentParser = lazy
            val postOperatorWs = if (operator.first.last().isJavaIdentifierPart()) not(identifierPart) + ws else ws
            currentParser.parser = parser + ws + opt((+operator.first + postOperatorWs + currentParser).generates {
                operator.second(it.pop(1), it.pop())
            })
            parser = currentParser
        }

        return parser
    }

    /**
     * Creates a parser that handles operators with different precedence, and creates InfixNodes from the parsed operators.
     * @param atomParser the smallest component, used as the inputs for the highest precedence operations.
     * @param operators one or more mappings from the operator sign to the generator function for the operator,
     *                  starting with the highest precedence operator and ending with the lowest one.
     *                  If no operators are given, the atomParser is returned as is.
     */
    fun <T> operatorNodeParser(atomParser: Parser, vararg operators: Pair<String, (T, T) -> Any>): Parser {
        var parser: Parser = atomParser
        for (operator in operators) {
            val currentParser = lazy
            val postOperatorWs = if (operator.first.last().isJavaIdentifierPart()) not(identifierPart) + ws else ws
            currentParser.parser = parser + ws + opt((+operator.first + postOperatorWs + currentParser).generates {
                InfixExpr(operator.first, it.pop(1), it.pop(), operator.second as (Any?, Any?) -> Any?)
            })
            parser = currentParser
        }

        return parser
    }

    /**
     * Creates a parser that parses functions with parameters defined in the specified name to function map,
     * and creates a function node for them using the provided functionNodeBuilder.
     */
    fun functionParser(functions: FunctionRegistry,
                       parameterParser: Parser,
                       parameterBlockStart: Parser = +"(",
                       parameterSeparator: Parser = +",",
                       parameterBlockEnd: Parser = +")",
                       allowWhitespace: Boolean = true,
                       endWithWhitespace: Boolean = true): Parser {

        return functions.createFunctionParser(
                parameterParser,
                if (allowWhitespace) ws else autoMatch,
                parameterBlockStart,
                parameterSeparator,
                parameterBlockEnd,
                endWithWhitespace
        )
    }


}