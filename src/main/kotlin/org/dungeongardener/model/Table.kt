package org.dungeongardener.model

import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Expression
import java.util.*

/**
 * Used for defining various random tables
 */
class Table<T>(val name: String,
               val roll: Expression,
               val startIndex: Int = 1,
               val description: String? = null,
               val entries: MutableList<TableEntry<T>> = ArrayList()) {

    data class TableEntry<T>(val content: T, val span: Int = 1)

    fun addEntry(content: T, span: Int = 1) {
        entries.add(TableEntry(content, span))
    }

    fun randomize(context: Context, modifier: Double = 0.0): T? {

        if (entries.isEmpty()) return null

        // Roll a random value
        val rolledValue = (roll.evaluate<Double>(context) + modifier).toInt()

        // Loop through table
        var index = startIndex
        for (entry in entries) {
            index += entry.span

            // Return the content for the entry that we hit
            if (rolledValue < index) return entry.content
        }

        // Cap to last entry
        return entries.last().content
    }

}