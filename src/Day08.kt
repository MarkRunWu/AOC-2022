import java.lang.Integer.max

enum class Direction(val raw: Int) {
    Left(0),
    Top(1),
    Right(2),
    Bottom(3)
}

fun main() {

    fun dumpMaxTreeMap(maxTreeMap: Array<Array<Array<Int>>>, direction: Direction) {
        for (i in maxTreeMap.indices) {
            for (j in maxTreeMap[i].indices) {
                print("[${maxTreeMap[i][j][direction.raw]}]")
            }
            println()
        }
        println()
    }

    fun part1(input: List<String>): Int {
        val maxTreeMap = Array(input.size) {
            Array(input.first().length) {
                Array(4) { 0 }
            }
        }
        for (i in input.indices) {
            for (j in input[i].indices) {
                val value = input[i][j].digitToInt()
                if (i == 0 || j == 0 || i == input.lastIndex || j == input[i].lastIndex) {
                    maxTreeMap[i][j][Direction.Left.raw] = value
                    maxTreeMap[i][j][Direction.Top.raw] = value
                    continue
                }
                maxTreeMap[i][j][Direction.Left.raw] =
                    max(maxTreeMap[i][j - 1][Direction.Left.raw], value)
                maxTreeMap[i][j][Direction.Top.raw] =
                    max(maxTreeMap[i - 1][j][Direction.Top.raw], value)
            }
        }
        for (i in input.indices.reversed()) {
            for (j in input[i].indices.reversed()) {
                val value = input[i][j].digitToInt()
                if (i == 0 || j == 0 || i == input.lastIndex || j == input[i].lastIndex) {
                    maxTreeMap[i][j][Direction.Right.raw] = value
                    maxTreeMap[i][j][Direction.Bottom.raw] = value
                    continue
                }
                maxTreeMap[i][j][Direction.Right.raw] =
                    max(maxTreeMap[i][j + 1][Direction.Right.raw], value)
                maxTreeMap[i][j][Direction.Bottom.raw] =
                    max(maxTreeMap[i + 1][j][Direction.Bottom.raw], value)
            }
        }

        var count = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (i == 0 || j == 0 || i == input.lastIndex || j == input[i].lastIndex) {
                    count++
                } else {
                    val value = input[i][j].digitToInt()
                    if ((value == maxTreeMap[i][j][Direction.Left.raw] &&
                                value > maxTreeMap[i][j - 1][Direction.Left.raw]) ||
                        (value == maxTreeMap[i][j][Direction.Top.raw] &&
                                value > maxTreeMap[i - 1][j][Direction.Top.raw]) ||
                        (value == maxTreeMap[i][j][Direction.Right.raw] &&
                                value > maxTreeMap[i][j + 1][Direction.Right.raw]) ||
                        (value == maxTreeMap[i][j][Direction.Bottom.raw]
                                && value > maxTreeMap[i + 1][j][Direction.Bottom.raw])
                    ) {
                        count++
                    }
                }
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {

        val visibleTreesMap = Array(input.size) {
            Array(input.first().length) {
                Array(4) { 0 }
            }
        }

        val digitsInput = input.map { it ->
            it.map {
                it.digitToInt()
            }
        }

        for (i in digitsInput.indices) {
            for (j in digitsInput[i].indices) {
                var maxTreeHeight = -1
                for (k in i + 1 until digitsInput.size) {
                    if (digitsInput[k][j] > maxTreeHeight) {
                        maxTreeHeight = digitsInput[k][j]
                        visibleTreesMap[k][j][Direction.Top.raw]++
                    }
                }
            }
        }
        for (i in digitsInput.indices) {
            for (j in digitsInput[i].indices) {
                var maxTreeHeight = -1
                for (k in j + 1 until digitsInput[i].size) {
                    if (digitsInput[i][k] > maxTreeHeight) {
                        maxTreeHeight = digitsInput[i][k]
                        visibleTreesMap[i][k][Direction.Left.raw]++
                    }
                }
            }
        }
        for (i in digitsInput.indices.reversed()) {
            for (j in digitsInput[i].indices.reversed()) {
                var maxTreeHeight = -1
                for (k in i - 1 downTo 0) {
                    if (digitsInput[k][j] > maxTreeHeight) {
                        maxTreeHeight = digitsInput[k][j]
                        visibleTreesMap[k][j][Direction.Bottom.raw]++
                    }
                }
            }
        }
        for (i in digitsInput.indices.reversed()) {
            for (j in digitsInput[i].indices.reversed()) {
                var maxTreeHeight = -1
                for (k in j - 1 downTo 0) {
                    if (digitsInput[i][k] > maxTreeHeight) {
                        maxTreeHeight = digitsInput[i][k]
                        visibleTreesMap[i][k][Direction.Right.raw]++
                    }
                }
            }
        }

        return visibleTreesMap.maxOf { it ->
            it.maxOf {
                it[Direction.Top.raw] *
                        it[Direction.Left.raw] *
                        it[Direction.Right.raw] *
                        it[Direction.Bottom.raw]
            }
        }
    }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
