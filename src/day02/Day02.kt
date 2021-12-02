package day02

import parsePairsOfStringAndInt
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("day02/Day02")
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

fun part1(input: List<String>): Int {
    // parse ship commands from input
    val givenShipCommands = parseShipCommands(input)

    // set initial horizontal position and depth
    var horizontalPosition = 0
    var depth = 0
    // calculate horizontal position and depth
    givenShipCommands.forEach { shipCommand ->
        when(shipCommand.direction) {
            ShipMovement.Direction.FORWARD -> horizontalPosition += shipCommand.value
            ShipMovement.Direction.UP -> depth -= shipCommand.value
            ShipMovement.Direction.DOWN -> depth += shipCommand.value
        }
    }

    return horizontalPosition*depth
}

fun part2(input: List<String>): Int {
    val givenShipCommands = parseShipCommands(input)

    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    givenShipCommands.forEach { shipCommand ->
        when(shipCommand.direction) {
            ShipMovement.Direction.FORWARD -> {
                horizontalPosition += shipCommand.value
                depth += aim*shipCommand.value
            }
            ShipMovement.Direction.UP -> aim -= shipCommand.value
            ShipMovement.Direction.DOWN -> aim += shipCommand.value
        }
    }

    return horizontalPosition*depth
}

class ShipMovement(
    val direction: Direction,
    val value: Int
) {

    enum class Direction {
        FORWARD,
        UP,
        DOWN;

        override fun toString(): String {
            return when(this) {
                FORWARD -> "forward"
                UP -> "up"
                DOWN -> "down"
            }
        }

        companion object {
            fun fromString(directionValue: String): Direction {
                return when(directionValue) {
                    "forward" -> FORWARD
                    "up" -> UP
                    "down" -> DOWN
                    else -> throw IllegalArgumentException("Direction $directionValue is unknown")
                }
            }

        }
    }
}

fun parseShipCommands(input: List<String>): List<ShipMovement> {
    return parsePairsOfStringAndInt(
        input,
        " "
    ).map { shipCommand ->
        ShipMovement(ShipMovement.Direction.fromString(shipCommand.first), shipCommand.second)
    }
}