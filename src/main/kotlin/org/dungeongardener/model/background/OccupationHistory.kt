package org.dungeongardener.model.background

/**
 * History of one occupation that a character has had.
 */
data class OccupationHistory(val occupation: Occupation, var startAge: Int, var endAge: Int = startAge) {
}