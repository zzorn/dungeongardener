package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.ParsingNode
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

    fun parse(inputFile: File) : ParsingResult = parse(inputFile.readText())

    fun parse(input: String) : ParsingResult {
        val root = ParsingNode(input)
        if (parse(root)) {

            // DEBUG: println(root)

            // Generate result if parsing was successful
            return root.generateResults()
        }
        else {
            // Parse failed
            // TODO: Include info on why
            return ParsingFail()
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