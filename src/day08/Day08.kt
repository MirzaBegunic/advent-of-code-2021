package day08

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("day08/Day08")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results from my input data
    // 473
    // 1097568
}

fun part1(input: List<String>): Int {
    val uniqueFourDigitOutputValues = mutableListOf<String>()
    input.forEach { line ->
        line.split("|")[1].trimStart().split(" ").forEach { outputValue ->
            if (outputValue.length == 2 || outputValue.length == 3 || outputValue.length == 4 || outputValue.length == 7) {
                uniqueFourDigitOutputValues.add(line)
            }
        }
    }
    return uniqueFourDigitOutputValues.size
}

fun part2(input: List<String>): Int {
    var outputNumbersSum = 0
    input.forEach { line ->
        val splitInput = line.split(" | ")
        val uniqueSignalPatterns = splitInput[0].split(" ")
        val outputValues = splitInput[1].split(" ")
        // get patterns for digits 1 4 and 7
        var digitOnePattern = ""
        var digitFourPattern = ""
        var digitSevenPattern = ""
        uniqueSignalPatterns.forEach { signalPattern ->
            when(signalPattern.length) {
                2 -> digitOnePattern = signalPattern
                3 -> digitSevenPattern = signalPattern
                4 -> digitFourPattern = signalPattern
            }
        }
        // build output number
        val outputNumber = StringBuilder()
        outputValues.forEach { outputValue ->
            when(outputValue.length) {
                2 -> outputNumber.append("1")
                3 -> outputNumber.append("7")
                4 -> outputNumber.append("4")
                7 -> outputNumber.append("8")
                5 -> {
                    val fiveSegmentNumber = decodeFiveSegmentNumber(outputValue, digitOnePattern, digitFourPattern)
                    outputNumber.append("$fiveSegmentNumber")
                }
                6 -> {
                    val sixSegmentNumber = decodeSixSegmentNumber(outputValue, digitSevenPattern, digitFourPattern)
                    outputNumber.append("$sixSegmentNumber")
                }
            }
        }

        outputNumbersSum += outputNumber.toString().toInt()
    }
    return outputNumbersSum
}

private fun decodeFiveSegmentNumber(
    digitPattern: String,
    digitOnePattern: String,
    digitFourPattern: String
): Int {
    // count how many segments of given digit pattern are matching digit 1 and 4
    var numberOfMatchingSegmentsWithDigitOnePattern = 0
    var numberOfMatchingSegmentsWithDigitFourPattern = 0
    digitPattern.forEach { segment ->
        if (digitOnePattern.contains(segment)) {
            numberOfMatchingSegmentsWithDigitOnePattern++
        }
        if (digitFourPattern.contains(segment)) {
            numberOfMatchingSegmentsWithDigitFourPattern++
        }
    }
    // based on count of matching segments return correct number
    return if (numberOfMatchingSegmentsWithDigitOnePattern == 2) {
        3
    } else if (numberOfMatchingSegmentsWithDigitFourPattern == 3) {
        5
    } else {
        2
    }
}

private fun decodeSixSegmentNumber(
    digitPattern: String,
    digitSevenPattern: String,
    digitFourPattern: String
): Int {
    // count how many segments of given digit pattern are matching digit 7 and 4
    var numberOfMatchingSegmentsWithDigitSevenPattern = 0
    var numberOfMatchingSegmentsWithDigitFourPattern = 0
    digitPattern.forEach { segment ->
        if (digitSevenPattern.contains(segment)) {
            numberOfMatchingSegmentsWithDigitSevenPattern++
        }
        if (digitFourPattern.contains(segment)) {
            numberOfMatchingSegmentsWithDigitFourPattern++
        }
    }
    // based on count of matching segments return correct number
    return if (numberOfMatchingSegmentsWithDigitFourPattern == 4) {
        9
    } else if (numberOfMatchingSegmentsWithDigitSevenPattern == 2) {
        6
    } else {
        0
    }
}