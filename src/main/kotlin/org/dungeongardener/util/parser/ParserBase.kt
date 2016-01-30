package org.dungeongardener.util.parser

/**
 * Base class for parsers
 */
abstract class ParserBase : Parser {

    override var name: String = this.javaClass.simpleName

    final override fun parse(parent: ParsingNode): Boolean {
        // Create node for this parser, and add it to the parent
        val parserNode: ParsingNode = parent.addSubNode(this)

        // Try to parse with this parser
        val success = doParse(parserNode)

        if (!success) {
            // Update error message
            if (!parent.errorMessage.hasError()) {
                parent.errorMessage.update(parserNode)
            }

            // Roll back node for unsuccessful parse
            parent.removeSubNode()
        }
        else {
            parent.errorMessage.clear()
        }

        return success
    }

    abstract fun doParse(parserNode: ParsingNode): Boolean



}