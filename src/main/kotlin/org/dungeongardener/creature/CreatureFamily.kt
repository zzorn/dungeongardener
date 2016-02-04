package org.dungeongardener.creature

import java.util.*

/**
 * Related creatures
 */
data class CreatureFamily(
        val name: String,
        val parentFamily: CreatureFamily? = null
) {

    var creatures: MutableList<Creature> = ArrayList()

    private var _childFamilies: MutableList<CreatureFamily> = ArrayList()

    init {
        if (parentFamily != null) parentFamily._childFamilies.add(this)
    }

    val childFamilies: List<CreatureFamily>
        get() = _childFamilies

}