package org.dungeongardener.model.terrain

import java.util.*

/**
 *
 */
data class TerrainType(
        val name: String,
        val parentType: TerrainType? = null
) {

    private var _childTypes: MutableList<TerrainType> = ArrayList()

    init {
        if (parentType != null) parentType._childTypes.add(this)
    }

    val childTypes: List<TerrainType>
        get() = _childTypes
}