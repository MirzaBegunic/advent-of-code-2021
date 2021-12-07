package day07

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("day07/Day07")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results from my input data
    // 340987
    // 96987874
}

fun part1(input: List<String>): Int {
    // parse crabs positions and find max position
    val crabsPositions = input.first().split(",").map { it.toInt() }
    val maxCrabPosition = crabsPositions.maxOrNull() ?: 0

    // find lowest fuel consumption for available positions
    var lowestFuelConsumption = Int.MAX_VALUE

    for (desiredPosition in 0..maxCrabPosition) {
        val fuelConsumption = calculateFuelConsumption(crabsPositions, desiredPosition)
        if (fuelConsumption < lowestFuelConsumption) {
            lowestFuelConsumption = fuelConsumption
        }
    }
    return lowestFuelConsumption
}

fun part2(input: List<String>): Int {
    // parse crabs positions and find max position
    val crabsPositions = input.first().split(",").map { it.toInt() }
    val maxCrabPosition = crabsPositions.maxOrNull() ?: 0

    // find lowest fuel consumption for available positions
    var lowestFuelConsumption = Int.MAX_VALUE

    for (desiredPosition in 0..maxCrabPosition) {
        val fuelConsumption = calculateFuelConsumptionWithMovementWeight(crabsPositions, desiredPosition)
        if (fuelConsumption < lowestFuelConsumption) {
            lowestFuelConsumption = fuelConsumption
        }
    }
    return lowestFuelConsumption
}

// calculate fuel consumption where every movement costs one fuel unit
private fun calculateFuelConsumption(crabsPositions: List<Int>, desiredPosition: Int): Int {
    var fuelConsumption = 0
    crabsPositions.forEach { position ->
        var crabFuelConsumption = position-desiredPosition
        if (crabFuelConsumption < 0) {
            crabFuelConsumption *= (-1)
        }
        fuelConsumption += crabFuelConsumption
    }
    return fuelConsumption
}

// calculate fuel consumption where every movement costs one more fuel unit than the last one
private fun calculateFuelConsumptionWithMovementWeight(crabsPositions: List<Int>, desiredPosition: Int): Int {
    var fuelConsumption = 0
    crabsPositions.forEach { position ->
        var movementDelta = position-desiredPosition
        if (movementDelta < 0) {
            movementDelta *= (-1)
        }
        var crabFuelConsumption = 0
        if (movementDelta > 0) {
            for (movement in 1..movementDelta) {
                crabFuelConsumption += movement
            }
        }
        fuelConsumption += crabFuelConsumption
    }
    return fuelConsumption
}