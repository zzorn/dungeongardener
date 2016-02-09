package org.dungeongardener.model.background

import org.dungeongardener.model.skill.Skill

/**
 *
 */
interface BackgroundCallback {

    fun note(message: String)
    fun pay(message: String, gold: Double): Boolean
    fun skillExp(skill: Skill, exp: Double)

}