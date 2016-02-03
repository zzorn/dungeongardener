package org.dungeongardener.util.parser.parsers

import org.dungeongardener.util.parser.ASTNode
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.ParsingError

/**
 * If parsing of the input node does not work, fail the whole parsing
 */
class Cut(val parser: Parser) : Parser {

    override fun parse(parent: ASTNode): Boolean {
        // Release cached packrat structures
        // TODO

        if (!parser.parse(parent)) {
            // Parsing will fail
            throw ParsingError()
        }
        else {
            return true
        }
    }

    override var name: String
        get() = parser.name
        set(value) {
            parser.name = value
        }
}