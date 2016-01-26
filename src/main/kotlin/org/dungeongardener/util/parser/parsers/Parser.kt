package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ParsingContext
import java.io.File

/**
 * Interface for parser rules.  Parses some expression or atomic element.
 */
interface Parser {

    fun parse(inputFile: File) : List<Any>  = parse(inputFile.readText())

    fun parse(input: String) : List<Any> {
        val context = ParsingContext(input)
        parse(context)
        return context.getResults()
    }

    fun parse(context: ParsingContext): Boolean

}