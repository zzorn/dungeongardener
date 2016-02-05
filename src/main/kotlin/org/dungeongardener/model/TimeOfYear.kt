package org.dungeongardener.model

/**
 *
 */
enum class TimeOfYear(val monthFrom: Int, val monthToInclusive: Int) {

    LATE_WINTER(1, 2),
    SPRING(3, 4),
    EARLY_SUMMER(5, 6),
    LATE_SUMMER(7, 8),
    AUTUMN(9, 10),
    EARLY_WINTER(11, 12)

}