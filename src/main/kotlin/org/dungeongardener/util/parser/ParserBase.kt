package org.dungeongardener.util.parser

/**
 * Base class for parsers
 */
abstract class ParserBase : Parser {

    override var name: String = this.javaClass.simpleName

    final override fun parse(parent: ASTNode): Boolean {

        // Check if we have a previously cached entry
        val packRatCache = parent.packRatCache
        val packRatCacheKey = ASTNode.PackRatKey(this, parent.end)
        val entry = packRatCache.get(packRatCacheKey)

        val success = if (entry != null) {
            val previouslyParsedNode = entry.astNode
            if (previouslyParsedNode != null) {
                parent.addSubNode(previouslyParsedNode)
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
            val cacheEntry = ASTNode.PackRatEntry(null, parent.end)
            packRatCache.put(packRatCacheKey, cacheEntry)

            // Create node for this parser, and add it to the parent
            val parserNode: ASTNode = parent.addSubNode(this)

            // Try to parse with this parser
            if (doParse(parserNode)) {
                // If the parse succeeded, update the cache
                cacheEntry.astNode = parserNode
                cacheEntry.endPos = parserNode.end
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

    abstract fun doParse(parserNode: ASTNode): Boolean



}