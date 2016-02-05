package org.dungeongardener.model.item

import org.dungeongardener.generator.Generator
import org.flowutils.random.RandomSequence

/**
 * Changes the aspects of an item in some way.
 */
data class ItemModifier(val id: String,
                        val name: Generator<String>,
                        val description: Generator<String>) {

    /**
     * Apply this modification to the item.
     */
    fun apply(item: Item, parameters: Map<String, Any>? = null, random: RandomSequence? = null) {
        // TODO: Evaluate formulas for the various numerical stats
        // TODO: Evaluate a new name (and description) for the item based on the previous name
    }

}