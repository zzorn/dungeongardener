package org.dungeongardener.util.parser

import org.dungeongardener.util.genlang.nodes.FunExpr
import org.dungeongardener.util.genlang.nodes.Expression
import org.dungeongardener.util.parser.parsers.*
import org.flowutils.Check
import java.util.*

/**
 * Keeps track of registered functions, and allows registering functions.
 */
class FunctionRegistry(val functions: MutableMap<String, FuncEntry> = LinkedHashMap()) {
    val functionNames: Collection<String> = functions.keys

    data class FuncEntry(val name: String, val paramCount: Int, val nodeCreationFunction: (List<Any>) -> Any) {
        init {
            Check.identifier(name, "name")
        }

        fun createNode(parameterNodes: List<Any>): Any {
            if (paramCount != -1 && paramCount != parameterNodes.size) {
                throw ParsingError("Invalid number of parameters for function '$name', " +
                                   "expected $paramCount but got ${parameterNodes.size}")
            }

            return nodeCreationFunction(parameterNodes)
        }
    }

    fun add(funcEntry: FuncEntry) {
        functions.put(funcEntry.name, funcEntry)
    }

    /**
     * Adds a function that is encapsulated in a FunctionNode taking other FunctionNodes as parameters,
     * allowing evaluation of the resulting expression after parsing using a changing context.
     */
    fun <P> addFun(name: String, parameterCount: Int, function: (funContext: FunctionContext<P>) -> Any) {
        add(FuncEntry(name, parameterCount, { ps -> FunExpr(name, ps as List<Expression>, function as (FunctionContext<Any>) -> Any) }))
    }

    /**
     * Adds a function that directly uses the parsing result nodes as input and generates a result that gets pushed on the parsing result stack.
     */
    fun <P> addDirectFun(name: String, parameterCount: Int, function: (params: List<P>) -> Any) {
        add(FuncEntry(name, parameterCount, { ps -> function(ps as List<P>) }))
    }

    fun applyFunction(name: String, parameterNodes: List<Any>): Any {
        val funcEntry = functions.get(name) ?: throw ParsingError("Could not find function with name '$name'")
        return funcEntry.createNode(parameterNodes)
    }

    /**
     * Creates a parser that parses functions with parameters defined in the specified name to function map,
     * and creates a function node for them using the provided functionNodeBuilder.
     */
    fun createFunctionParser(parameterParser: Parser,
                             whitespace: Parser = CharParser(" \n\t"),
                             parameterBlockStart: Parser = +"(",
                             parameterSeparator: Parser = +",",
                             parameterBlockEnd: Parser = +")",
                             endWithWhitespace: Boolean = true): Parser {
        return SequenceParser(
                DynamicAnyOfStrings(functionNames).generatesMatchedText(),
                whitespace,
                parameterBlockStart,
                whitespace,
                Optional(
                        parameterParser,
                        whitespace,
                        ZeroOrMore(
                                parameterSeparator,
                                whitespace,
                                parameterParser,
                                whitespace
                        )
                ).generates { it.popCurrentNodeResults<Any>() },
                parameterBlockEnd,
                if (endWithWhitespace) whitespace else AutoMatch()
        ).generates {
            val parameters = it.pop<List<Any>>()
            val functionName = it.pop<String>()
            applyFunction(functionName, parameters)
        }
    }
}