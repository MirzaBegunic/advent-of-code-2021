import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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
