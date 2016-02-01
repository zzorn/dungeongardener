package org.dungeongardener.util.parser

/**
 * Base class for parsers
 */
abstract class ParserBase : Parser {

    override var name: String = this.javaClass.simpleName

    final override fun parse(parent: ParsingNode): Boolean {

        // Check if we have a previously cached entry
        val packRatCache = parent.packRatCache
        val packRatCacheKey = ParsingNode.PackRatKey(this, parent.end)
        val entry = packRatCache.get(packRatCacheKey)

        val success = if (entry != null) {
            if (entry.parsedNode != null) {
                parent.addSubNode(entry.parsedNode)
                true
            }
            else {
                // Update error message for left recursion
                //if (!parent.errorMessage.hasError()) {
                    parent.errorMessage.leftRecursionError(this, parent.end, parent.input)
                //}

                false
            }
        }
        else {
            // Add null packrat node, for detecting left recursion
            packRatCache.put(packRatCacheKey, ParsingNode.PackRatEntry(null, parent.end))

            // Create node for this parser, and add it to the parent
            val parserNode: ParsingNode = parent.addSubNode(this)

            // Try to parse with this parser
            if (doParse(parserNode)) {
                // If the parse succeeded, cache the result
                packRatCache.put(packRatCacheKey, ParsingNode.PackRatEntry(parserNode, parserNode.end))
                true
            }
            else {
                // Update error message
                if (!parent.errorMessage.hasError()) {
                    parent.errorMessage.update(parserNode)
                }

                // Roll back node for unsuccessful parse
                parent.removeSubNode()

                false
            }
        }

        if (success) {
            // Clear error if we succeeded
            parent.errorMessage.clear()
        }

        return success
    }

    abstract fun doParse(parserNode: ParsingNode): Boolean



}