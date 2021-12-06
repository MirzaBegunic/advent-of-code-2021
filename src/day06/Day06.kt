package day06

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 5934UL)
    check(part2(testInput) == 26984457539UL)

    val input = readInput("day06/Day06")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results from my input data
    // 387413
    // 1738377086345
}

fun part1(input: List<String>): ULong {
    return calculateNumberOfLanterFish(input, 80)
}

fun part2(input: List<String>): ULong {
    return calculateNumberOfLanterFish(input, 256)
}

private fun calculateNumberOfLanterFish(input: List<String>, numberOfDays: Int): ULong {
    // create array that holds how many fish has timer at given value
    // index of array represents timer value
    val countersOfTimers = Array(9) { 0UL }
    // parse and initialize countersOfTimers array
    input[0].split(",").forEach { timer ->
        countersOfTimers[timer.toInt()]++
    }
    // tick over given numbers of days
    for (day in 1..numberOfDays) {
        tickOverOneDay(countersOfTimers)
    }
    // calculate number of fish by summing up counters on all timers
    var numberOfFish = 0UL
    countersOfTimers.forEach { counterOfTimer ->
        numberOfFish += counterOfTimer
    }

    return numberOfFish
}

private fun tickOverOneDay(counterOfTimers: Array<ULong>) {
    // all fish that have timer equal to zero will give new fish
    val numberOfFishReproducing = counterOfTimers[0]
    // decrement timers of every fish
    for (i in 0..7) {
        counterOfTimers[i] = counterOfTimers[i+1]
    }
    // all newly created fish have timer of 8
    counterOfTimers[8] = numberOfFishReproducing
    // all fish that created new fish reset their timer to 6
    counterOfTimers[6] += numberOfFishReproducing
}
