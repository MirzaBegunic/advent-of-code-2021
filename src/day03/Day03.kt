package day03

import filterBitArrayCollectionByCriteria
import findLeastCommonBitInColumn
import findMostCommonBitInColumn
import findMostCommonBitInColumns
import parseBitArrayCollection
import readInput
import reverseBitArray
import sumAllNumbers
import toInt

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("day03/Day03")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results for my input data
    // 3633500
    // 4550283
}

fun part1(input: List<String>): Int {
    val bitArraySize = input[0].length
    val bitArray = parseBitArrayCollection(input)
    val gammaRateBitSet = findMostCommonBitInColumns(bitArray, bitArraySize)
    val epsilonRateBitSet = reverseBitArray(gammaRateBitSet, bitArraySize)
    val gamma =gammaRateBitSet.toInt(bitArraySize)
    val epsilon = epsilonRateBitSet.toInt(bitArraySize)
    return epsilon*gamma
}

fun part2(input: List<String>): Int {
    val bitArraySize = input[0].length
    val bitArray = parseBitArrayCollection(input)
    // calculating oxygen generator rating
    val filteredBitArrayForOxygenRating =
        filterBitArrayCollectionByCriteria(bitArray, bitArraySize) { bitArrayCollection, columnIndex ->
            findMostCommonBitInColumn(bitArrayCollection, columnIndex)
        }
    var oxygenGeneratorRating = filteredBitArrayForOxygenRating.sumAllNumbers(bitArraySize)
    // calculating CO2 scrubber rating
    val filteredBitArrayForCO2Rating =
        filterBitArrayCollectionByCriteria(bitArray, bitArraySize) { bitArrayCollection, columnIndex ->
            findLeastCommonBitInColumn(bitArrayCollection, columnIndex)
        }
    var co2ScrubberRating = filteredBitArrayForCO2Rating.sumAllNumbers(bitArraySize)
    return oxygenGeneratorRating*co2ScrubberRating
}

