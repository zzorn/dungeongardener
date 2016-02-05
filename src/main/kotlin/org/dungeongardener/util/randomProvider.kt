package org.dungeongardener.util

import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift

/**
 * A static default random provider.
 * Not thread safe.
 * Do not call setSeed, as this may be shared by many users.
 */
val randomProvider: RandomSequence = XorShift()