package day09

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("day09/Day09")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results from my input data
    // 535
    // 1122700
}

fun part1(input: List<String>): Int {
    val heightMap = HeightMap(input)
    val riskLevel = heightMap.getRiskLevel()
    return riskLevel
}

fun part2(input: List<String>): Int {
    val heightMap = HeightMap(input)
    return heightMap.getTop3BasinsSizeMultiplied()
}

class HeightMap(
    input: List<String>
) {
    private val mapMatrix: Array<Array<Int>>
    private val width: Int
    private val height: Int
    init {
        width = input[0].length
        height = input.size
        mapMatrix = Array(height) { Array(width) { 0 } }

        input.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { segmentIndex, segment ->
                mapMatrix[lineIndex][segmentIndex] = segment.digitToInt()
            }
        }
    }

    fun getRiskLevel(): Int {
        var riskLevel = 0
        val lowestPoints = getLowestPoints()
        lowestPoints.forEach { lowestPoint ->
            riskLevel += lowestPoint.value+1
        }
        return riskLevel
    }
    
    fun getTop3BasinsSizeMultiplied(): Int {
        val lowestPoints = getLowestPoints()
        val basins = mutableListOf<Int>()
        lowestPoints.forEach { lowestPoint ->
            val basinSize = getBasinSize(lowestPoint)
            basins.add(basinSize)
        }
        basins.sortByDescending { it }
        return basins[0]*basins[1]*basins[2]
    }

    private fun getBasinSize(point: Point): Int {
        val basinPoints = mutableListOf<Point>()
        getBasinOfPoint(basinPoints, point, null)

        return basinPoints.size
    }

    private fun getBasinOfPoint(basinPoints: MutableList<Point>, point: Point, parentPoint: Point?) {
        if (basinPoints.contains(point)) {
            return
        }

        basinPoints.add(point)
        val left = getPointToTheLeft(point.columnIndex, point.rowIndex)
        val right = getPointToTheRight(point.columnIndex, point.rowIndex)
        val up = getPointUp(point.columnIndex, point.rowIndex)
        val down = getPointDown(point.columnIndex, point.rowIndex)

        if (parentPoint == null) {
            if (left != null && left.value != 9) {
                getBasinOfPoint(basinPoints, left, point)
            }
            if (right != null && right.value != 9) {
                getBasinOfPoint(basinPoints, right, point)
            }
            if (up != null && up.value != 9) {
                getBasinOfPoint(basinPoints, up, point)
            }
            if (down != null && down.value != 9) {
                getBasinOfPoint(basinPoints, down, point)
            }
        } else {
            if (left != null && left.value != 9 && left != parentPoint) {
                getBasinOfPoint(basinPoints, left, point)
            }
            if (right != null && right.value != 9 && right != parentPoint) {
                getBasinOfPoint(basinPoints, right, point)
            }
            if (up != null && up.value != 9 && up != parentPoint) {
                getBasinOfPoint(basinPoints, up, point)
            }
            if (down != null && down.value != 9 && down != parentPoint) {
                getBasinOfPoint(basinPoints, down, point)
            }
        }
    }


    private fun getLowestPoints(): List<Point> {
        val lowestPoints = mutableListOf<Point>()
        mapMatrix.forEachIndexed { columnIndex, column ->
            column.forEachIndexed { rowIndex, value ->
                val surroundingLowPoints = mutableListOf<Point>()
                getPointToTheLeft(columnIndex, rowIndex)?.let { leftValue ->
                    surroundingLowPoints.add(leftValue)
                }
                getPointToTheRight(columnIndex, rowIndex)?.let { rightValue ->
                    surroundingLowPoints.add(rightValue)
                }
                getPointUp(columnIndex, rowIndex)?.let { upValue ->
                    surroundingLowPoints.add(upValue)
                }
                getPointDown(columnIndex, rowIndex)?.let { downValue ->
                    surroundingLowPoints.add(downValue)
                }

                surroundingLowPoints.sortBy { it.value }
                val lowestSurroundingLowPoint = surroundingLowPoints.first()

                if (value < lowestSurroundingLowPoint.value) {
                    lowestPoints.add(Point(value, rowIndex, columnIndex))
                }
            }

        }
        return lowestPoints
    }

    private fun getPointToTheLeft(columnIndex: Int, rowIndex: Int): Point? {
        if (rowIndex == 0) {
            return null
        }

        return Point(mapMatrix[columnIndex][rowIndex-1], rowIndex-1, columnIndex)
    }

    private fun getPointToTheRight(columnIndex: Int, rowIndex: Int): Point? {
        if (rowIndex == width-1) {
            return null
        }

        return Point(mapMatrix[columnIndex][rowIndex+1], rowIndex+1, columnIndex)
    }

    private fun getPointUp(columnIndex: Int, rowIndex: Int): Point? {
        if (columnIndex == 0) {
            return null
        }

        return Point(mapMatrix[columnIndex-1][rowIndex], rowIndex, columnIndex-1)
    }

    private fun getPointDown(columnIndex: Int, rowIndex: Int): Point? {
        if (columnIndex == height-1) {
            return null
        }

        return Point(mapMatrix[columnIndex+1][rowIndex], rowIndex, columnIndex+1)
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        mapMatrix.forEach { column ->
            column.forEach { value ->
                stringBuilder.append("$value")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}

data class Point(
    val value: Int,
    val rowIndex: Int,
    val columnIndex: Int
)