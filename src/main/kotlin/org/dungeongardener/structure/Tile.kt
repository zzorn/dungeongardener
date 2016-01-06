package org.dungeongardener.structure

import org.dungeongardener.Entity
import org.dungeongardener.util.Direction
import java.util.*

/**
 *
 */
data class Tile(val surfaces: MutableMap<Direction, SurfaceType?> = EnumMap(Direction::class.java),
                var filled: Boolean = false) {

    // TODO: Allow things that can be attached to surfaces (e.g. paintings, shields, decorations and so on).
    // TODO: Windows and doors of various types could be in the same category.  (Perhaps also machine ports?)

    private var entities_: List<Entity>? = null

    val entities: List<Entity>
        get() = entities_ ?: Collections.emptyList()



}
