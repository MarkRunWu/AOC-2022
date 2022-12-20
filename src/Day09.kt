import java.lang.Math.abs
import java.lang.Math.max
import java.text.Normalizer.normalize
import kotlin.math.min
import kotlin.math.sign

private operator fun Pair<Int, Int>.plus(movement: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + movement.first, this.second + movement.second)
}

enum class Marker {
    Tail,
    Head,
    Origin,
    Empty
}

fun main() {
    data class Position(var x: Int, var y: Int) {
        fun getNextStepPosition(target: Position): Position? {
            if (kotlin.math.abs(target.x - this.x) > 1) {
                return Position(
                    this.x + if (target.x - this.x > 0) 1 else -1,
                    if (target.y != this.y)
                        target.y
                    else
                        this.y
                )
            }
            if (kotlin.math.abs(target.y - this.y) > 1) {
                return Position(
                    if (target.x != this.x) target.x else this.x,
                    this.y + if (target.y - this.y > 0) 1 else -1
                )
            }
            return null
        }
    }

    fun dumpMap(map: Array<Array<Pair<Int, Marker>>>) {
        map.map { it ->
            println(it.map { v ->
                when (v.second) {
                    Marker.Origin -> {
                        "S"
                    }

                    Marker.Tail -> {
                        "#"
                    }

                    Marker.Head -> {
                        "@"
                    }

                    else -> {
                        "."
                    }
                }
            }.joinToString("") { it })
        }
        println()
    }

    fun part1(input: List<String>): Int {
        val movements = input.map {
            val commands = it.split(' ')
            when (commands[0]) {
                "R" -> Pair(commands[1].toInt(), 0)
                "U" -> Pair(0, commands[1].toInt())
                "L" -> Pair(-commands[1].toInt(), 0)
                "D" -> Pair(0, -commands[1].toInt())
                else -> throw IllegalStateException("unknown command: ${commands[0]}")
            }
        }
        val headVisitedPositionList = ArrayList<Position>()
        val tailVisitedPositionList = ArrayList<Position>()

        var headPos = Position(0, 0)
        var tailPos = Position(0, 0)
        headVisitedPositionList.add(headPos)
        tailVisitedPositionList.add(tailPos)
        for (movement in movements) {
            val xSteps = kotlin.math.abs(movement.first)
            val ySteps = kotlin.math.abs(movement.second)
            if (xSteps > 0) {
                val step = if (movement.first > 0) 1 else -1
                for (i in 0 until xSteps) {
                    headPos = Position(headPos.x + step, headPos.y)
                    headVisitedPositionList.add(headPos)
                    val nextTailPos = tailPos.getNextStepPosition(headPos)
                    if (nextTailPos != null) {
                        tailPos = nextTailPos
                        tailVisitedPositionList.add(nextTailPos)
                    }
                }
            }
            if (ySteps > 0) {
                val step = if (movement.second > 0) 1 else -1
                for (i in 0 until ySteps) {
                    headPos = Position(headPos.x, headPos.y + step)
                    headVisitedPositionList.add(headPos)
                    val nextTailPos = tailPos.getNextStepPosition(headPos)
                    if (nextTailPos != null) {
                        tailPos = nextTailPos
                        tailVisitedPositionList.add(nextTailPos)
                    }
                }
            }
        }
        val minX = min(headVisitedPositionList.minOf { it.x }, tailVisitedPositionList.minOf { it.x })
        val minY = min(headVisitedPositionList.minOf { it.y }, tailVisitedPositionList.minOf { it.y })
        val normalizedHeadPositionList = headVisitedPositionList.map {
            Position(it.x - minX, it.y - minY)
        }
        val normalizedTailPositionList = tailVisitedPositionList.map {
            Position(it.x - minX, it.y - minY)
        }
        val origin = Position(-minX, -minY)

        val maxX = normalizedHeadPositionList.maxOf { it.x }.coerceAtLeast(normalizedTailPositionList.maxOf { it.x })
        val maxY = normalizedHeadPositionList.maxOf { it.y }.coerceAtLeast(normalizedTailPositionList.maxOf { it.y })

        var map = Array(
            maxY + 1
        ) {
            Array(maxX + 1) {
                Pair(0, Marker.Empty)
            }
        }

        map[origin.y][origin.x] = Pair(0, Marker.Origin)

        for (p in normalizedHeadPositionList.indices) {
            map[normalizedHeadPositionList[p].y][normalizedHeadPositionList[p].x] = Pair(
                p,
                Marker.Head
            )
        }
        dumpMap(map)

        map = Array(
            maxY + 1
        ) {
            Array(maxX + 1) {
                Pair(0, Marker.Empty)
            }
        }
        map[origin.y][origin.x] = Pair(0, Marker.Origin)
        for (p in normalizedTailPositionList.indices) {
            map[normalizedTailPositionList[p].y][normalizedTailPositionList[p].x] = Pair(
                p,
                Marker.Tail
            )
        }
        dumpMap(map)



        return map.sumOf { it -> it.count { it.second == Marker.Tail } }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
