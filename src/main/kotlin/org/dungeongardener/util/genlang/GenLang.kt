package org.dungeongardener.util.genlang

import org.dungeongardener.util.genlang.nodes.*
import org.dungeongardener.util.numberexpr.DiceExpr
import org.dungeongardener.util.numberexpr.dice
import org.dungeongardener.util.parser.FunctionRegistry
import org.dungeongardener.util.parser.LanguageBase
import org.dungeongardener.util.parser.Parser
import org.flowutils.MathUtils
import org.flowutils.MathUtils.*
import org.flowutils.Symbol
import java.lang.Math.*


/**
 * Generic language
 */
// TODO: Add statements, assignmentStatements, imports, string/other generators?  Maybe use other char than + for generator concatenation.
// TODO: Maybe use different language to load a file depending on the postfix, but collect the loaded things in the same context?
// TODO: Add lists and maps
// TODO: Possible to either parse program that defines named expressions, or to just parse a single expression.
class GenLang() : LanguageBase<Expression>() {

    val functions = FunctionRegistry()

    val expression = lazy

    val reference = identifier.generates { ReferenceExpr(Symbol.get(it.pop())) } + ws

    val numberConstant = (double + ws).generates { ConstantExpr(it.pop()) }

    val boolConstant = any(
            keyword("true").generates { ConstantExpr(true) },
            keyword("false").generates { ConstantExpr(false) }
    )

    val dice = (positiveInteger + char("dD") + positiveInteger + ws).generates {
        DiceExpr(it.pop(), it.pop())
    }

    val parens = (+"(" - expression - ")" + ws).generates { ParensExpr(it.pop()) }

    val ifExpr = (keyword("if") + "(" - expression + ")" - expression + keyword("else") + expression).generates {
        IfElseExpr(it.pop(2), it.pop(1), it.pop())
    }

    val atom = any(parens, dice, boolConstant, numberConstant, ifExpr, functionParser(functions, expression), reference)

    val unaryMinus = any((+"-" + ws + atom).generates { UnaryExpr<Double>("-", it.pop(), { b -> -b}) }, atom)

    val unaryNot = any((keyword("not") + unaryMinus).generates { UnaryExpr<Boolean>("not", it.pop(), { b -> !b}) }, unaryMinus)

    val numExpression = operatorNodeParser<Double>(unaryNot,
            "*" to { a, b -> a * b },
            "/" to { a, b -> a / b },
            "+" to { a, b -> a + b },
            "-" to { a, b -> a - b }
    )

    val comparisonExpression = operatorNodeParser<Double>(numExpression,
            "<" to { a, b -> a < b },
            ">" to { a, b -> a > b },
            "<=" to { a, b -> a <= b },
            ">=" to { a, b -> a >= b }
    )

    val equivalenceExpression = operatorNodeParser<Any>(comparisonExpression,
            "==" to { a, b -> a == b },
            "!=" to { a, b -> a != b }
    )

    val boolExpression = operatorNodeParser<Boolean>(equivalenceExpression,
            "and" to { a, b -> a && b },
            "or" to { a, b -> a || b }
    )



    val assignment = ((identifier - "=" - expression).generates {  }).cut()

    val statement = any(assignment)

    val packageRef = (identifier + zeroOrMore(+"." + identifier)).generates { it.text } + ws

    val import = keyword("import") + packageRef

    val program = zeroOrMore(import) + zeroOrMore(statement)

    init {
        expression.parser = boolExpression

        addEndOfLineComment("#")
        addEndOfLineComment("//")
        addBlockComment("/*", "*/")

        addBuiltinMathFunctions()
    }

    override val parser: Parser = ws + expression + endOfInput

    private fun addBuiltinMathFunctions() {

        functions.addFun<Double>("random", 2) { it.context.random.nextDouble(it.a, it.b) }
        functions.addFun<Double>("randomInt", 2) { it.context.random.nextInt(it.a.toInt(), it.b.toInt()).toDouble() }
        functions.addFun<Double>("gauss", 2) { it.context.random.nextGaussian(it.a, it.b) }
        functions.addFun<Double>("dice", 2) { it.context.random.dice(it.b.toInt(), it.a.toInt()).toDouble() }
        functions.addFun<Double>("singleDice", 1) { it.context.random.dice(it.a.toInt()).toDouble() }

        functions.addFun<Double>("max", 2) { if (it.a >= it.b) it.a else it.b }
        functions.addFun<Double>("min", 2) { if (it.a <= it.b) it.a else it.b }
        functions.addFun<Double>("clamp", 3) { clamp(it.a, it.b, it.c) }
        functions.addFun<Double>("clamp0to1", 1) { clamp0To1(it.x) }
        functions.addFun<Double>("wrap", 3) { wrap(it.a, it.b, it.c) }
        functions.addFun<Double>("wrap0to1", 1) { wrap0To1(it.x) }
        functions.addFun<Double>("abs", 1) { abs(it.x) }
        functions.addFun<Double>("signum", 1) { signum(it.x) }

        functions.addFun<Double>("round", 1) { MathUtils.round(it.x) }
        functions.addFun<Double>("ceil", 1) { ceil(it.x) }
        functions.addFun<Double>("floor", 1) { floor(it.x) }

        functions.addFun<Double>("mod", 2) { it.a % it.b }
        functions.addFun<Double>("modPositive", 2) { modPositive(it.a, it.b) }

        functions.addFun<Double>("exp", 1) { exp(it.x) }
        functions.addFun<Double>("log", 1) { log(it.x) }
        functions.addFun<Double>("log10", 1) { log10(it.x) }
        functions.addFun<Double>("logN", 2) { log(it.x, it.y) }
        functions.addFun<Double>("sqrt", 1) { sqrt(it.x) }
        functions.addFun<Double>("pow", 2) { pow(it.x, it.y) }

        functions.addFun<Double>("sin", 1) { sin(it.x) }
        functions.addFun<Double>("cos", 1) { cos(it.x) }
        functions.addFun<Double>("tan", 1) { tan(it.x) }
        functions.addFun<Double>("atan2", 2) { Math.atan2(it.x, it.y) }

        functions.addFun<Double>("sigmoid", 2) { sigmoid(it.a, it.b) }
        functions.addFun<Double>("sigmoid0to1", 2) { sigmoidZeroToOne(it.a, it.b) }

        functions.addFun<Double>("mix", 3) { mix(it.a, it.b, it.c) }
        functions.addFun<Double>("mixAndClamp", 3) { mixAndClamp(it.a, it.b, it.c) }
        functions.addFun<Double>("mixSmooth", 3) { mixSmooth(it.a, it.b, it.c) }
        functions.addFun<Double>("relPos", 3) { relPos(it.a, it.b, it.c) }
        functions.addFun<Double>("map", 5) { map(it.a, it.b, it.c, it.d, it.e) }
        functions.addFun<Double>("mapAndClamp", 5) { mapAndClamp(it.a, it.b, it.c, it.d, it.e) }
        functions.addFun<Double>("mapSmooth", 5) { mapSmooth(it.a, it.b, it.c, it.d, it.e) }

        functions.addFun<Double>("average", -1) { var sum = 0.0; for (p in it.parameters) sum += p; sum / it.parameterCount }
    }



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