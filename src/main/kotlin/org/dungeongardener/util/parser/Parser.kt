package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.parsers.Cut
import org.dungeongardener.util.parser.parsers.GeneratingParser
import org.dungeongardener.util.parser.parsers.ProcessingParser
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

    fun parse(inputFile: File, debugOutput: Boolean = false) : ParsingResult = parse(inputFile.readText(), inputFile.name, debugOutput = debugOutput)

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

    fun parse(parent: ASTNode): Boolean


    /**
     * If parsing this parser does not succeed, fail the whole parsing.
     * Using this helps optimize the parser by reducing backtracking when it is known that nothing else will work.
     */
    fun cut(): Parser = Cut(this)

    fun generatesMatchedText(): Parser = GeneratingParser(this, {it.text})
    infix fun generates(generator: (GeneratorContext) -> Any): Parser = GeneratingParser(this, generator)
    infix fun process(processor: (GeneratorContext) -> Unit): Parser = ProcessingParser(this, processor)

    /**
     * Names the parser and returns it
     */
    infix fun named(name: String): Parser {
        this.name = name
        return this
    }

}