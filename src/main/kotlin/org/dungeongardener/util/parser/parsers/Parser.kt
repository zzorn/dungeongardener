package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.GeneratorContext
import org.dungeongardener.util.parser.ParsingContext
import org.dungeongardener.util.parser.result.ParsingFail
import org.dungeongardener.util.parser.result.ParsingResult
import java.io.File

/**
 * Interface for parser rules.  Parses some expression or atomic element.
 */
interface Parser {

    fun parse(inputFile: File) : ParsingResult = parse(inputFile.readText())

    fun parse(input: String) : ParsingResult {
        val context = ParsingContext(input)
        if (parse(context)) {
            return context.getResult()
        }
        else {
            return ParsingFail()
        }
    }

    fun parse(context: ParsingContext): Boolean

    infix fun generates(generator: (GeneratorContext) -> Any): Parser = GeneratingParser(this, generator)
    infix fun process(processor: (GeneratorContext) -> Unit): Parser = ProcessingParser(this, processor)

}