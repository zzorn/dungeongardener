package org.dungeongardener.structure

import org.dungeongardener.util.Int3

/**
 * A building of som kind.
 * Grid based.
 * May be connected to other structures or the outdoors at some exits.
 * @param size size of the structure in grid cells
 */
class Structure(val gridSize_meters: Double = 1.0,
                val gridHeight_meters: Double = 1.0,
                val size: Int3) {

    /**
     * @return Tile at the specified grid cell in the structure, or null if the structure
     * does not contain that location.
     */
    // TODO: Allow special blocks that fill a tile (or several adjacent ones) as well - e.g. structures with higher (or lower) detail level, machine blocks, doors? etc
    // TODO furniture would probably be entities?  Or maybe blocks?
    fun getTile(pos: Int3): Tile? {
        // TODO
        return null
    }

}