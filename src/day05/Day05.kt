package day05

import readInput


fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("day05/Day05")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results for my input data
    // 7297
    // 21038

}

fun part1(input: List<String>): Int {
    // parse hydro thermal lines
    val hydroThermalLines = parseHydroThermalLines(input)
    // filter horizontal and vertical lines
    val horizontalHydroThermalLines = hydroThermalLines.filter { it.type == Line.Type.HORIZONTAL }
    val verticalHydroThermalLines = hydroThermalLines.filter { it.type == Line.Type.VERTICAL }
    // find max x and y coordinates from lines
    var maxXCoordinate = 0
    var maxYCoordinate = 0
    horizontalHydroThermalLines.forEach { horizontalHydroThermalLine ->
        val lineMaxXCoordinate = horizontalHydroThermalLine.maxXCoordinate()
        if (lineMaxXCoordinate > maxXCoordinate) {
            maxXCoordinate = lineMaxXCoordinate
        }
        val lineMaxYCoordinate = horizontalHydroThermalLine.maxYCoordinate()
        if (lineMaxYCoordinate > maxYCoordinate) {
            maxYCoordinate = lineMaxYCoordinate
        }
    }
    verticalHydroThermalLines.forEach { verticalHydroThermalLine ->
        val lineMaxXCoordinate = verticalHydroThermalLine.maxXCoordinate()
        if (lineMaxXCoordinate > maxXCoordinate) {
            maxXCoordinate = lineMaxXCoordinate
        }
        val lineMaxYCoordinate = verticalHydroThermalLine.maxYCoordinate()
        if (lineMaxYCoordinate > maxYCoordinate) {
            maxYCoordinate = lineMaxYCoordinate
        }
    }
    // create thermal diagram with width = maxX+1 and height = maxY+1
    val thermalDiagram = ThermalDiagram(maxXCoordinate+1, maxYCoordinate+1)

    // add horizontal and vertical lines to thermal diagram
    horizontalHydroThermalLines.forEach { horizontalHydroThermalLine ->
        thermalDiagram.insertLine(horizontalHydroThermalLine)
    }

    verticalHydroThermalLines.forEach { verticalHydroThermalLine ->
        thermalDiagram.insertLine(verticalHydroThermalLine)
    }
    // return number of areas where two or more lines overlap
    return thermalDiagram.numberOfDangerAreas()
}

fun part2(input: List<String>): Int {
    // parse hydro thermal lines
    val hydroThermalLines = parseHydroThermalLines(input)
    // find max x and y coordinates from lines
    var maxXCoordinate = 0
    var maxYCoordinate = 0
    hydroThermalLines.forEach { horizontalHydroThermalLine ->
        val lineMaxXCoordinate = horizontalHydroThermalLine.maxXCoordinate()
        if (lineMaxXCoordinate > maxXCoordinate) {
            maxXCoordinate = lineMaxXCoordinate
        }
        val lineMaxYCoordinate = horizontalHydroThermalLine.maxYCoordinate()
        if (lineMaxYCoordinate > maxYCoordinate) {
            maxYCoordinate = lineMaxYCoordinate
        }
    }
    // create thermal diagram with width = maxX+1 and height = maxY+1
    val thermalDiagram = ThermalDiagram(maxXCoordinate+1, maxYCoordinate+1)
    // add lines to thermal diagram
    hydroThermalLines.forEach { hydroThermalLine ->
        thermalDiagram.insertLine(hydroThermalLine)
    }
    // return number of areas where two or more lines overlap
    return thermalDiagram.numberOfDangerAreas()
}

private fun parsePointCoordinates(pointCoordinatesInput: String): Point {
    val pointCoordinates = pointCoordinatesInput.split(",")
    val pointX = pointCoordinates[0].toInt()
    val pointY = pointCoordinates[1].toInt()
    return Point(pointX, pointY)
}

private fun parseHydroThermalLines(input: List<String>): List<Line> {
    return input.map { inputLine ->
        val lineCoordinates = inputLine.split(" -> ")
        val firstPoint = parsePointCoordinates(lineCoordinates[0])
        val secondPoint = parsePointCoordinates(lineCoordinates[1])
        Line(firstPoint, secondPoint)
    }
}


class ThermalDiagram(
    width: Int,
    height: Int
) {
    private var diagram: Array<Array<Int>>

    init {
        diagram = Array(height) {
            Array(width,) {
                0
            }
        }
    }

    /**
     * Insert thermal line into diagram
     */
    fun insertLine(thermalLine: Line) {
        when(thermalLine.type) {
            Line.Type.HORIZONTAL -> {
                val yIndex = thermalLine.firstPoint.yCoordinate
                val xIndexes = if (thermalLine.firstPoint.xCoordinate <= thermalLine.secondPoint.xCoordinate) {
                    val startIndex = thermalLine.firstPoint.xCoordinate
                    val endIndex = thermalLine.secondPoint.xCoordinate
                    Pair(startIndex, endIndex)
                } else {
                    val startIndex = thermalLine.secondPoint.xCoordinate
                    val endIndex = thermalLine.firstPoint.xCoordinate
                    Pair(startIndex, endIndex)
                }
                for (xIndex in xIndexes.first .. xIndexes.second) {
                    diagram[yIndex][xIndex]++
                }
            }
            Line.Type.VERTICAL -> {
                val xIndex = thermalLine.firstPoint.xCoordinate
                val yIndexes = if (thermalLine.firstPoint.yCoordinate <= thermalLine.secondPoint.yCoordinate) {
                    val startIndex = thermalLine.firstPoint.yCoordinate
                    val endIndex = thermalLine.secondPoint.yCoordinate
                    Pair(startIndex, endIndex)
                } else {
                    val startIndex = thermalLine.secondPoint.yCoordinate
                    val endIndex = thermalLine.firstPoint.yCoordinate
                    Pair(startIndex, endIndex)
                }
                for (yIndex in yIndexes.first .. yIndexes.second) {
                    diagram[yIndex][xIndex]++
                }
            }
            Line.Type.DIAGONAL -> {
                if (thermalLine.firstPoint.xCoordinate < thermalLine.secondPoint.xCoordinate) {
                    if (thermalLine.firstPoint.yCoordinate < thermalLine.secondPoint.yCoordinate) {
                        for((counter, _) in (thermalLine.firstPoint.xCoordinate .. thermalLine.secondPoint.xCoordinate).withIndex()) {
                            diagram[thermalLine.firstPoint.yCoordinate+counter][thermalLine.firstPoint.xCoordinate+counter]++
                        }
                    } else {
                        for((counter, _) in (thermalLine.firstPoint.xCoordinate .. thermalLine.secondPoint.xCoordinate).withIndex()) {
                            diagram[thermalLine.firstPoint.yCoordinate-counter][thermalLine.firstPoint.xCoordinate+counter]++
                        }
                    }
                } else {
                    if (thermalLine.firstPoint.yCoordinate < thermalLine.secondPoint.yCoordinate) {
                        for((counter, _) in (thermalLine.firstPoint.xCoordinate downTo  thermalLine.secondPoint.xCoordinate).withIndex()) {
                            diagram[thermalLine.firstPoint.yCoordinate+counter][thermalLine.firstPoint.xCoordinate-counter]++
                        }
                    } else {
                        for((counter, _) in (thermalLine.firstPoint.xCoordinate downTo  thermalLine.secondPoint.xCoordinate).withIndex()) {
                            diagram[thermalLine.firstPoint.yCoordinate-counter][thermalLine.firstPoint.xCoordinate-counter]++
                        }
                    }
                }
            }
        }
    }

    /**
     * Find number of danger areas. Danger area is point where two or more thermal lines cross
     */
    fun numberOfDangerAreas(): Int {
        var dangerZonesCounter = 0
        diagram.forEach { diagramLine ->
            diagramLine.forEach { pointValue ->
                if (pointValue >= 2) {
                    dangerZonesCounter++
                }
            }
        }
        return dangerZonesCounter
    }

    override fun toString(): String {
        val diagramString = StringBuilder()
        diagram.forEach { diagramLine ->
            diagramLine.forEach { fieldValue ->
                if (fieldValue == 0) {
                    diagramString.append(".")
                } else {
                    diagramString.append("$fieldValue")
                }
            }
            diagramString.append("\n")
        }
        return diagramString.toString()
    }
}

class Line(
    val firstPoint: Point,
    val secondPoint: Point
) {
    val type: Type

    init {
        type = if (isHorizontal()) {
            Type.HORIZONTAL
        } else if (isVertical()) {
            Type.VERTICAL
        } else {
            Type.DIAGONAL
        }
    }

    private fun isHorizontal() = firstPoint.yCoordinate == secondPoint.yCoordinate
    private fun isVertical() = firstPoint.xCoordinate == secondPoint.xCoordinate
    fun maxXCoordinate(): Int {
        return if (firstPoint.xCoordinate >= secondPoint.xCoordinate) {
            firstPoint.xCoordinate
        } else {
            secondPoint.xCoordinate
        }
    }
    fun maxYCoordinate(): Int {
        return if (firstPoint.yCoordinate >= secondPoint.yCoordinate) {
            firstPoint.yCoordinate
        } else {
            secondPoint.yCoordinate
        }
    }

    override fun toString(): String {
        return "(${firstPoint.xCoordinate},${firstPoint.yCoordinate}) -> (${secondPoint.xCoordinate},${secondPoint.yCoordinate})"
    }

    enum class Type{
        HORIZONTAL,
        VERTICAL,
        DIAGONAL
    }
}

class Point(
    val xCoordinate: Int,
    val yCoordinate: Int
)