package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.parsers.*
import org.dungeongardener.util.parser.result.ParseSuccess
import org.dungeongardener.util.parser.result.ParsingFail
import org.dungeongardener.util.parser.result.ParsingResult
import java.io.File

/**
 * Interface for parser rules.  Parses some expression or atomic element.
 */
// TODO: Support char range parser
// TODO: Somehow convert strings or chars or char ranges automatically to a parser??  Or use datatype Any, and pass in those, but that is unintuitive
interface Parser {

    /**
     * Used for making error messages and debugging more clear
     */
    var name: String

    /**
     * Parse the content of the input file and return a parse success or failure object.
     */
    fun parse(inputFile: File, debugOutput: Boolean = false) : ParsingResult = parse(inputFile.readText(), inputFile.name, debugOutput = debugOutput)

    /**
     * Parse the input text and return a parse success or failure object.
     */
    fun parse(input: String, inputName: String = "input", debugOutput: Boolean = false) : ParsingResult {
        val root = ASTNode(input, errorMessage = ParsingFail(inputName), debugOutput = debugOutput)

        val success = try {parse(root)} catch (e: ParsingError) {
            root.errorMessage.exception = e
            false
        }

        if (success) {
            // Generate result if parsing was successful
            return root.generateResults()
        }
        else {
            return root.errorMessage
        }
    }

    /**
     * Parse the input text and return the first result.
     * @throws ParsingError if there is no results, or if the parse failed.
     */
    fun <T>parseFirst(input: String, inputName: String = "input", debugOutput: Boolean = false) : T {
        val result = parse(input, inputName, debugOutput)

        when (result) {
            is ParsingFail -> throw ParsingError(result.toString())
            is ParseSuccess -> {
                if (result.results.isEmpty()) throw ParsingError("No results were generated")
                else return result.results.first() as T
            }
            else -> throw IllegalStateException("Unknown result type ${result.javaClass}")
        }
    }

    /**
     * Parse the text of the input file and return the first result.
     * @throws ParsingError if there is no results, or if the parse failed.
     */
    fun <T>parseFirst(inputFile: File, debugOutput: Boolean = false) : T = parseFirst(inputFile.readText(), inputFile.name, debugOutput = debugOutput)


    /**
     * Used internally.
     */
    fun parse(parent: ASTNode): Boolean


    /**
     * If parsing this parser does not succeed, fail the whole parsing.
     * Using this helps optimize the parser by reducing backtracking when it is known that nothing else will work.
     */
    fun cut(): Parser = Cut(this)

    fun generatesMatchedText(): Parser = GeneratingParser(this, {it.text})
    fun generatesTrimmedMatchedText(): Parser = GeneratingParser(this, {it.text.trim()})
    fun generatesContentList(): Parser = GeneratingParser(this, { it.popCurrentNodeResults<Any?>() })

    infix fun generates(generator: (GeneratorContext) -> Any): Parser = GeneratingParser(this, generator)
    infix fun process(processor: (GeneratorContext) -> Unit): Parser = ProcessingParser(this, processor)

    /**
     * Add two parsers to create a sequence
     */
    operator fun plus(other: Parser): SequenceParser {
        return SequenceParser(this, other)
    }

    /**
     * Add string parser to create a sequence
     */
    operator fun plus(other: String): SequenceParser {
        return SequenceParser(this, StringParser(other))
    }

    /**
     * Add char parser to create a sequence
     */
    operator fun plus(other: CharSequence): SequenceParser {
        return SequenceParser(this, CharParser(other))
    }

    /**
     * Names the parser and returns it
     */
    infix fun named(name: String): Parser {
        this.name = name
        return this
    }

}