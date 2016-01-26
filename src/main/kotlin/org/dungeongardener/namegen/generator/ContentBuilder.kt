package org.dungeongardener.namegen.generator

/**
 * Something that node contents are added to, and that can build a result object based on them.
 */
interface ContentBuilder<T, R> {

    fun add(content: T)

    fun build(): R
}