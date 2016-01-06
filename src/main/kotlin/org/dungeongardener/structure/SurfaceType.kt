package org.dungeongardener.structure

import java.awt.Color

/**
 *
 */
data class SurfaceType(val name: String,
                       val color: Color = Color.GRAY,
                       val shape: SurfaceShape = SurfaceShape.FLAT,
                       val blocking: Boolean = true) {

    // TODO: Add support for opening (door / window / archway)
}