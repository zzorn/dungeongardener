package org.dungeongardener.creature

/**
 *
 */
enum class Commonality(val degree: Int, val weight: Double) {

    ExtremelyCommon(4, 16.0),
    VeryCommon(3, 8.0),
    Common(2, 4.0),
    FairlyCommon(1, 2.0),
    Normal(0, 1.0),
    FairlyRare(-1, 0.5),
    Rare(-2, 0.25),
    VeryRare(-3, 0.125),
    ExtremelyRare(-4, 1.0/16.0),

}