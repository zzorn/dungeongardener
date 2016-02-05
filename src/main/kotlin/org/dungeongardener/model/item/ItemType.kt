package org.dungeongardener.model.item

import org.dungeongardener.generator.Generator
import org.dungeongardener.util.randomProvider

/**
 * A type of item.
 * name is usually just a fixed string, but the description can change more often
 * (for example if the item type is a painting, the description would randomize the description of it based on the seed)
 */
data class ItemType(val id: String,
                    val name: Generator<String>,
                    val description: Generator<String>,
                    val baseValue_sp: Generator<Double>,
                    val weight_kg: Generator<Double>,
                    val length_m: Generator<Double>,
                    val family: ItemFamily) {

    /**
     * Creates a new instance of this item type, with the specified modifiers and seed (default to no modifiers and a random seed)
     */
    fun createInstance(vararg modifiers: ItemModifier, seed: Long = randomProvider.nextLong()): Item {
        return Item(this, seed, modifiers = modifiers.toMutableSet())
    }
}