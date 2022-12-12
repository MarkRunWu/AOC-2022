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
        return 0;
    }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
