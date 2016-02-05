package org.dungeongardener.model

/**
 *
 */
enum class TimeOfDay(val hourFrom: Int, val hourTo: Int) {
    MORNING(6, 12),
    DAY(12, 18),
    EVENING(18, 24),
    NIGHT(0, 6),
}