package org.dungeongardener.util.parser

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

    fun parse(inputFile: File) : ParsingResult = parse(inputFile.readText(), inputFile.name)

    fun parse(input: String, inputName: String = "input") : ParsingResult {
        val root = ParsingNode(input, errorMessage = ParsingFail(inputName))
        if (parse(root)) {

            // DEBUG: println(root)

            // Generate result if parsing was successful
            return root.generateResults()
        }
        else {
            // Parse failed
            println(root.errorMessage)
            return root.errorMessage
        }
    }

    fun parse(parent: ParsingNode): Boolean


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