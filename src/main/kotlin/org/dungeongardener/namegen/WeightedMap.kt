package org.dungeongardener.namegen

import org.flowutils.Check
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift
import java.util.*

/**
 * Contains entries and weights associated with them.  Can return a random entry with probability proportional to its weight.
 */
class WeightedMap<T>(initialEntries: Map<T, Double>? = null) {

    private final val defaultRandom = XorShift()
    private val entries: MutableMap<T, Double> = LinkedHashMap()
    private var totalWeight = 0.0

    val readOnlyEntries: Map<T, Double>
       get() = entries

    init {
        if (initialEntries != null) addEntries(initialEntries)
    }

    fun setEntry(entry: T, relativeWeight: Double) {
        removeEntry(entry)

        if (relativeWeight > 0) {
            entries.put(entry, relativeWeight)
            totalWeight += relativeWeight
        }
    }

    fun removeEntry(entry: T) {
        val removed = entries.remove(entry)
        if (removed != null) totalWeight -= removed
    }

    fun addEntries(entries: Map<T, Double>) {
        entries.forEach { addEntry(it.key, it.value) }
    }

    fun addEntry(entry: T, relativeAdditionalWeight: Double) {
        Check.positiveOrZero(relativeAdditionalWeight, "relativeAdditionalWeight")
        val weight = Math.max(0.0, relativeAdditionalWeight + (entries.get(entry) ?: 0.0))
        entries.put(entry, weight)
        totalWeight += relativeAdditionalWeight
    }

    fun randomEntry(random: RandomSequence = defaultRandom): T {
        var randomPosition = random.nextDouble(totalWeight)

        for (entry in entries) {
            randomPosition -= entry.value
            if (randomPosition <= 0) return entry.key
        }

        throw IllegalStateException("Random value was $randomPosition, total weight was $totalWeight, but did not find any entry.  Sum of weights: " + entries.values.reduce { a, b ->  a+b})
    }

    override fun toString(): String {
        return "WeightedMap(${entries.toString()})"
    }
}