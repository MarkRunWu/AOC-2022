fun main() {
    data class Movement(val offsetX: Int, val offsetY: Int);

    data class Position(var x: Int, var y: Int) {
        fun getNextStepPosition(target: Position): Position? {
            val nextX = if (kotlin.math.abs(target.x - this.x) > 1) {
                this.x + if (target.x - this.x > 0) 1 else -1
            } else null
            val nextY = if (kotlin.math.abs(target.y - this.y) > 1) {
                this.y + if (target.y - this.y > 0) 1 else -1
            } else null
            if (nextX != null || nextY != null) {
                return Position(nextX ?: target.x, nextY ?: target.y)
            }
            return null
        }
    }

    fun dumpMap(map: Array<Array<Boolean>>) {
        map.map { it ->
            println(it.map { v ->
                if (v) "#" else "."
            }.joinToString("") { it })
        }
        println()
    }

    fun parseCommands(input: List<String>): List<Movement> {
        return input.map {
            val commands = it.split(' ')
            when (commands[0]) {
                "R" -> Movement(commands[1].toInt(), 0)
                "U" -> Movement(0, commands[1].toInt())
                "L" -> Movement(-commands[1].toInt(), 0)
                "D" -> Movement(0, -commands[1].toInt())
                else -> throw IllegalStateException("unknown command: ${commands[0]}")
            }
        }
    }

    fun simulate(ropeCount: Int, headMovements: List<Movement>): List<Array<Array<Boolean>>> {
        val currentRopePositions = Array(ropeCount) {
            Position(0, 0)
        }
        val ropeMoveResults = Array(ropeCount) {
            ArrayList<Position>()
        }
        ropeMoveResults.forEachIndexed { index, positions ->
            positions.add(currentRopePositions[index].copy())
        }
        for (movement in headMovements) {
            val xSteps = kotlin.math.abs(movement.offsetX)
            val ySteps = kotlin.math.abs(movement.offsetY)
            if (xSteps > 0) {
                val step = if (movement.offsetX > 0) 1 else -1
                for (i in 0 until xSteps) {
                    currentRopePositions[0].x += step
                    ropeMoveResults[0].add(currentRopePositions[0].copy())
                    for (k in 1 until ropeMoveResults.size) {
                        val curPos = currentRopePositions[k]
                        val nextPos = curPos.getNextStepPosition(currentRopePositions[k - 1])
                        if (nextPos != null) {
                            currentRopePositions[k] = nextPos.copy()
                            ropeMoveResults[k].add(nextPos.copy())
                        }
                    }
                }
            }
            if (ySteps > 0) {
                val step = if (movement.offsetY > 0) 1 else -1
                for (i in 0 until ySteps) {
                    currentRopePositions[0].y += step
                    ropeMoveResults[0].add(currentRopePositions[0].copy())
                    for (k in 1 until ropeMoveResults.size) {
                        val curPos = currentRopePositions[k]
                        val nextPos = curPos.getNextStepPosition(currentRopePositions[k - 1])
                        if (nextPos != null) {
                            currentRopePositions[k] = nextPos.copy()
                            ropeMoveResults[k].add(nextPos.copy())
                        }
                    }
                }
            }
        }

        val maxX = ropeMoveResults.maxOf { it -> it.maxOf { it.x } }
        val maxY = ropeMoveResults.maxOf { it -> it.maxOf { it.y } }
        val minX = ropeMoveResults.minOf { it -> it.minOf { it.x } }
        val minY = ropeMoveResults.minOf { it -> it.minOf { it.y } }
        val width = maxX - minX + 1
        val height = maxY - minY + 1

        val ropeMovementMapList = ropeMoveResults.map {
            Array(height) {
                Array(width) {
                    false
                }
            }
        }

        for (i in ropeMoveResults.indices) {
            for (pos in ropeMoveResults[i]) {
                ropeMovementMapList[i][pos.y - minY][pos.x - minX] = true
            }
        }
        return ropeMovementMapList
    }

    fun part1(input: List<String>): Int {
        val movements = parseCommands(input)
        val result = simulate(2, movements)
        dumpMap(result.last())
        return result.last().sumOf { it -> it.count { it } }
    }

    fun part2(input: List<String>): Int {
        val movements = parseCommands(input)
        val result = simulate(10, movements)
        dumpMap(result.last())
        return result.last().sumOf { it -> it.count { it } }
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
