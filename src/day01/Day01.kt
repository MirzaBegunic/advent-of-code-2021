package day01

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val depthValues = input.map { it.toInt() }

        var depthIncreasedFromPreviousDepthCounter = 0

        for (depthCollectionCounter: Int in 1 until depthValues.size) {
            val currentDepth = depthValues[depthCollectionCounter]
            val previousDepth = depthValues[depthCollectionCounter-1]
            if (currentDepth > previousDepth) {
                depthIncreasedFromPreviousDepthCounter++
            }
        }

        return depthIncreasedFromPreviousDepthCounter
    }

    fun part2(input: List<String>): Int {
        var depthSumIncreasedFromPreviousDepthSumCounter = 0

        val depthValues = input.map { it.toInt() }

        for (depthCollectionCounter: Int in 1..depthValues.size-3) {
            val previousDepthSum = depthValues.subList(depthCollectionCounter-1, depthCollectionCounter+2).sum()
            val currentDepthSum = depthValues.subList(depthCollectionCounter, depthCollectionCounter+3).sum()
            if (currentDepthSum > previousDepthSum) {
                depthSumIncreasedFromPreviousDepthSumCounter++
            }
        }
        return depthSumIncreasedFromPreviousDepthSumCounter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("day01/Day01")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results from my input data
    // 1766
    // 1797
}
