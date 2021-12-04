import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.math.pow

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Takes collection of strings that represent pair values of string and int, and returns list of string and int pairs.
 */
fun parsePairsOfStringAndInt(pairedStringsAndInts: Collection<String>, delimiter: String): List<Pair<String,Int>> {
    val result = mutableListOf<Pair<String, Int>>()
    pairedStringsAndInts.forEach { pairedStringAndInt ->
        val (stringValue, intValue) = pairedStringAndInt.split(delimiter)
        val convertedInt = intValue.toInt()
        result.add(Pair(stringValue, convertedInt))
    }
    return result
}

/**
 * Takes collection of strings that represent arrays of strings, and returns list of BitSet
 */
fun parseBitArrayCollection(bitArrayCollection: Collection<String>): List<BitSet> {
    val bitArray = bitArrayCollection.map { bitArrayString ->
        val bitSet = BitSet(bitArrayString.length)
        bitArrayString.forEachIndexed { index, bitValue ->
            bitSet[index] = bitValue.digitToInt() == 1
        }
        bitSet
    }
    return bitArray
}

/**
 * Takes collection of bit set and bit array size, and returns bit set that represents most common bits in columns
 */
fun findMostCommonBitInColumns(bitArrayCollection: Collection<BitSet>, bitArraySize: Int): BitSet {
    val mostCommonBitInColumn = mutableListOf<Int>()
    for (mostCommonBitIndex in 0 until bitArraySize) {
        mostCommonBitInColumn.add(mostCommonBitIndex, 0)
    }

    bitArrayCollection.forEach { bitArray ->
        for (mostCommonBitIndex in 0 until bitArraySize) {
            if (bitArray[mostCommonBitIndex]) {
                mostCommonBitInColumn[mostCommonBitIndex]++
            } else {
                mostCommonBitInColumn[mostCommonBitIndex]--
            }
        }
    }

    val mostCommonBitArray = BitSet(bitArraySize)
    mostCommonBitInColumn.forEachIndexed { index, mostCommonBit ->
        mostCommonBitArray[index] = mostCommonBit > 0
    }

    return mostCommonBitArray
}

/**
 * Takes collection of bit set, and finds most common bit in given column by column index
 */
fun findMostCommonBitInColumn(bitArrayCollection: Collection<BitSet>, columnIndex: Int): Boolean {
    var numberOfOnes = 0
    var numberOfZeros = 0
    bitArrayCollection.forEach { bitArray ->
        if (bitArray[columnIndex]) {
            numberOfOnes++
        } else {
            numberOfZeros++
        }
    }
    return numberOfOnes >= numberOfZeros
}

/**
 * Takes collection of bit set, and finds least common bit in given column by column index
 */

fun findLeastCommonBitInColumn(
    bitArrayCollection: Collection<BitSet>,
    columnIndex: Int
) = !findMostCommonBitInColumn(bitArrayCollection, columnIndex)

/**
 * Takes bit set and bit array size, and returns reversed bit set
 */
fun reverseBitArray(bitArray: BitSet, bitArraySize: Int): BitSet {
    val reversedBitSet = BitSet(bitArraySize)
    for(bitArrayIndex in 0 until bitArraySize) {
        reversedBitSet[bitArrayIndex] = !bitArray[bitArrayIndex]
    }
    return reversedBitSet
}

/**
 * Converts bit set to int.
 */
fun BitSet.toInt(bitArraySize: Int): Int {
    var intValue = 0
    for (bitArrayIndex in 0 until bitArraySize) {
        if(this[bitArrayIndex]) {
            intValue += (2.0).pow(bitArraySize-1-bitArrayIndex).toInt()
        }
    }
    return intValue
}

/**
 * Prints bit set
 */
fun BitSet.print(bitArraySize: Int) {
    println()
    for(bitArrayIndex in 0 until bitArraySize) {
        if(this[bitArrayIndex]) {
            print("1")
        } else {
            print("0")
        }
    }
    println()
}

/**
 * Takes collection of bit set, and return list of bit set filtered by given criteria
 */
fun filterBitArrayCollectionByCriteria(
    bitArrayCollection: Collection<BitSet>,
    bitArraySize: Int,
    criteria: (bitArrayCollection: Collection<BitSet>, columnIndex: Int) -> Boolean
): List<BitSet> {
    var filteredBitArray = bitArrayCollection.toMutableList()
    var bitArrayColumnIndex = 0
    while (filteredBitArray.size > 1 && bitArrayColumnIndex < bitArraySize) {
        val criteriaResult = criteria(filteredBitArray, bitArrayColumnIndex)
        filteredBitArray = filteredBitArray.filter { bitArray ->
            bitArray[bitArrayColumnIndex] == criteriaResult
        }.toMutableList()
        bitArrayColumnIndex++
    }
    return filteredBitArray
}

/**
 * Convert all numbers in collection of bit set, and return their sum
 */
fun Collection<BitSet>.sumAllNumbers(bitArraySize: Int): Int {
    var sum = 0
    this.forEach { bitArray ->
        sum += bitArray.toInt(bitArraySize)
    }
    return sum
}