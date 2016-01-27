package org.dungeongardener.util.parser

/**
 *
 */
interface GeneratorContext {

    val matched: String
    fun pushResult(result: Any)
    fun popResult(): Any

}