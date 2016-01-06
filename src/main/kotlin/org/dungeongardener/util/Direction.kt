package org.dungeongardener.util

/**
 *
 */
enum class Direction(val longName: String,
                     val dir: Int3) {

    N("north",      Int3(0,1,0)),
    E("east",       Int3(1,0,0)),
    S("south",      Int3(0,-1,0)),
    W("west",       Int3(-1,0,0)),
    U("up",         Int3(0,0,1)),
    D("down",       Int3(0,0,-1))


}