package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.parser.Language
import org.dungeongardener.util.parser.Parser
import org.flowutils.Symbol


class NumExprLanguage() : Language<NumExpr>() {

    val comment = parser("comment") {
        +"#" + zeroOrMoreCharsExcept("\n") + "\n"
    }

    override val whitespace = parser("whitespace") {
        zeroOrMore(oneOrMoreChars(" \n\t"), comment)
    }

    val positiveIntegerNumber = parserWithWhitespace("positiveIntegerNumber") {
        oneOrMoreChars('0'..'9').generates {
            it.text.toInt()
        }
    }

    val number = parserWithWhitespace("number") {
        (opt(+"-") + oneOrMoreChars('0'..'9') + opt("." + oneOrMoreChars('0'..'9'))).generates {
            it.text.toDouble()
        }
    }

    val term = lazy
    val expression = lazy
    val factor = lazy

    val parens = +"(" - expression - ")" + ws

    val unaryMinus = (+"-" + ws + factor).generates { NegExpr(it.pop()) }

    val constant = (number + ws).generates { ConstantExpr(it.pop()) }

    val dice = (positiveIntegerNumber + char("dD") + positiveIntegerNumber + ws).generates {
        DiceExpr(it.pop(), it.pop())
    }

    val reference = identifier.generates { ReferenceExpr(Symbol.get(it.text)) } + ws

    init {
        factor.parser = any(parens, unaryMinus, dice, constant, reference)

        term.parser = factor - opt(any(
                (+"*" - term).generates { MulExpr(it.pop(1), it.pop()) },
                (+"/" - term).generates { DivExpr(it.pop(1), it.pop()) }
        )) + ws

        expression.parser = term - opt(any(
                    (+"+" - expression).generates { SumExpr(it.pop(1), it.pop()) },
                    (+"-" - expression).generates { SubExpr(it.pop(1), it.pop()) }
        )) + ws
    }

    override val parser: Parser = ws + expression + endOfInput




}






/*
import org.flowutils.Symbol
import org.parboiled.Action
import org.parboiled.BaseParser
import org.parboiled.Context
import org.parboiled.Rule
import org.parboiled.errors.ErrorUtils
import org.parboiled.parserunners.RecoveringParseRunner
import org.parboiled.support.Var
import java.io.File
import java.util.*

/**
 *
 */
// TODO: Fails, because methods are not lazy as bytecode is not instrumented.  Write own parser library?
class NumExprParser(): BaseParser<NumExpr>() {

    interface ParseResult
    data class ParseError(val errorMessage: String, val parsedInput: String): ParseResult
    data class ParseSuccess(val expression: NumExpr): ParseResult

    protected data class NamedExpression(val id: Symbol, val expression: NumExpr): NumExpr {
        override fun evaluate(context: NumContext): Double {
            throw IllegalStateException("Named Expressions do not return a value")
        }
    }

    fun parse(text: String): ParseResult {
        val result = RecoveringParseRunner<NumExpr>(inputLine()).run(text)
        if (result.hasErrors()) return ParseError(ErrorUtils.printParseErrors(result), text)
        else return ParseSuccess(result.resultValue)
    }

    fun parseExpr(text: String): NumExpr {
        val parsed = parse(text)
        return when (parsed) {
            is ParseError -> throw IllegalArgumentException("Parse error: ${parsed.errorMessage} for input '$text'")
            is ParseSuccess -> parsed.expression
            else -> throw IllegalStateException("Unknown result type $parsed")
        }
    }

    fun parseFileContent(text: String): NumContext {
        val result = RecoveringParseRunner<NumExpr>(inputFile()).run(text)
        if (result.hasErrors()) throw IllegalArgumentException("Parse error: ${ErrorUtils.printParseErrors(result)} for input '$text'")
        else {
            val expressions = LinkedHashMap<Symbol, NumExpr>()
            for (n in result.parseTreeRoot.children) {
                val e = n as NamedExpression
                expressions.put(e.id, e.expression)
            }
            return NumExpressions(expressions)
        }
    }

    fun parseFile(file: File): NumContext {
        return parseFileContent(file.readText())
    }

    fun inputFile(): Rule {
        return Sequence(ZeroOrMore(namedExpression()), EOI)
    }

    fun inputLine(): Rule {
        return Sequence(expression(), EOI)
    }

    fun namedExpression(): Rule {
        return Sequence(
                identifier(),
                object : Action<NumExpr> {
                    override fun run(context: Context<NumExpr>?): Boolean {

                        return push(ReferenceExpr(Symbol.get(match())))
                    }
                },
                "= ",
                expression(),
                whiteSpace(),
                ZeroOrMore("\n "),
                object : Action<NumExpr> {
                    override fun run(context: Context<NumExpr>?): Boolean {
                        return push(NamedExpression((pop(1) as ReferenceExpr).referenceId, pop()))
                    }
                }
                )
    }

    fun expression(): Rule = Sequence(
                term(),
                ZeroOrMore(
                        "+ ",
                        term(),
                        push(SumExpr(pop(1), pop()))
                )
        )

    fun term() = Sequence(
                factor(),
                ZeroOrMore(
                        "* ",
                        factor(),
                        push(MulExpr(pop(1), pop()))
                )
        )

    fun factor() = FirstOf(dice(), number(), clamp(), gaussian(), random(), reference(), parens())


    fun dice() : Rule {
        val count = Var<Int>()
        return Sequence(
            OneOrMore(digit()),

            object : Action<NumExpr> {
                override fun run(context: Context<NumExpr>?): Boolean {
                    return count.set(Integer.parseInt(matchOrDefault("1")))
                }
            },

            FirstOf("D", "d"),

            OneOrMore(digit()),

            object : Action<NumExpr> {
                override fun run(context: Context<NumExpr>?): Boolean {
                    return push(DiceExpr(count.get(), Integer.parseInt(matchOrDefault("6"))))
                }
            },
            whiteSpace()
            )
        }


    fun clamp() = Sequence(
                "clamp ",
                "( ",
                expression(),
                ", ",
                number(),
                ", ",
                number(),
                ") ",
                push(ClampExpr(pop(2), pop(1), pop())),
                whiteSpace()
        )

    fun gaussian() = Sequence(
                "gaussian ",
                "( ",
                number(),
                ", ",
                number(),
                ") ",
                push(GaussianExpr(pop(1), pop())),
                whiteSpace()
        )

    fun random() = Sequence(
                "random ",
                "( ",
                number(),
                ", ",
                number(),
                ") ",
                push(RandomExpr(pop(1), pop())),
                whiteSpace()
        )

    fun reference() = Sequence(
                identifier(),
                push(ReferenceExpr(Symbol.get(match()))),
                whiteSpace()
        )

    fun parens() = Sequence("( ", expression(), ") ")

    fun number() = Sequence(
                // we use another Sequence in the "Number" Sequence so we can easily access the input text matched
                // by the three enclosed rules with "match()" or "matchOrDefault()"
                Sequence(
                        Optional('-'),
                        OneOrMore(digit()),
                        Optional('.', OneOrMore(digit()))
                ),

                // the matchOrDefault() call returns the matched input text of the immediately preceding rule
                // or a default string (in this case if it is run during error recovery (resynchronization))
            object : Action<NumExpr> {
                override fun run(context: Context<NumExpr>?): Boolean {

                    return push(ConstantExpr(java.lang.Double.parseDouble(matchOrDefault("0"))))
                }
            },
                whiteSpace()
        )

    fun digit() = CharRange('0', '9')

    fun char() = FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'))

    fun identifier() = Sequence(char(), ZeroOrMore(FirstOf(char(), digit())))

    fun whiteSpace() : Rule = ZeroOrMore(AnyOf(" \t"))

    // We redefine the rule creation for string literals to automatically match trailing whitespace if the string
    // literal ends with a space character, this way we don't have to insert extra whitespace() rules after each
    // character or string literal
    override protected fun fromStringLiteral(string: String): Rule {
        return if (string.endsWith(" ")) {
            Sequence(String(string.substring(0, string.length - 1)), whiteSpace())
        }
        else {
            String(string)
        };
    }
}
*/