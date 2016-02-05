package org.dungeongardener.model.item

import org.dungeongardener.util.randomProvider
import java.util.*

/**
 * An unique item.
 * @param quality reflects the build quality of the item.
 */
// TODO: Some classes of items have additional properties (
// TODO e.g. artifacts (=manufactured items), food, weapons, wearables, clothing, armor, etc.)
// TODO: Should we create own subclasses for those?  Or some kind of entity system type approach?
// TODO: If we do an entity system type of approach, are modifiers appropriate? Should they be component type specific?
data class Item(val type: ItemType,
                val seed: Long = randomProvider.nextLong(),
                val quality: Double = 0.5,
                private val modifiers: MutableSet<ItemModifier> = LinkedHashSet()) {

    var name: String = type.id
    var description: String = ""
    var weight_kg: Double = 0.0
    var length_m: Double = 0.0
    var value_sp: Double = 0.0

    init {
        update()
    }

    fun addModifier(modifier: ItemModifier) {
        if (!modifiers.contains(modifier)) {
            modifiers.add(modifier)
            update()
        }
    }

    fun removeModifier(modifier: ItemModifier) {
        if (modifiers.contains(modifier)) {
            modifiers.remove(modifier)
            update()
        }
    }

    private fun update() {
        val parameters = mutableMapOf<String, Any>("seed" to seed, "quality" to quality)
        name = type.name.generate(parameters)
        description = type.description.generate(parameters)
        weight_kg = type.weight_kg.generate(parameters)
        length_m = type.length_m.generate(parameters)
        value_sp = type.baseValue_sp.generate(parameters)

        for (modifier in modifiers) {
            parameters.put("name", name)
            parameters.put("itemType", type)
            parameters.put("weight", weight_kg)
            parameters.put("length", length_m)
            parameters.put("value", value_sp)
            modifier.apply(this, parameters)
        }
    }

}