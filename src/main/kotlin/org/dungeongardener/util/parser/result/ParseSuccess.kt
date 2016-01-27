package org.dungeongardener.util.parser.result

/**
 *
 */
data class ParseSuccess(val results: List<Any>) : ParsingResult {
    override val success: Boolean = true

}