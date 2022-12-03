import java.lang.Math.abs

fun main() {
    fun getPriority(char: Char): Int {
        return if (char.code >= 'a'.code) {
            char.code - 'a'.code
        } else {
            char.code - 'A'.code + 26
        } + 1
    }

    fun part1(input: List<String>): Int {
        return input.map { it ->
            val a = it.substring(0 until it.length / 2)
            val b = it.substring(it.length / 2 until it.length)
            a.find {
                b.contains(it)
            }
        }.sumOf {
            getPriority(it!!)
        }
    }

    fun part2(input: List<String>): Int {
        return input.mapIndexed { index, s ->
            Pair(index, s)
        }.groupBy { it.first / 3 }
            .map {
                it.value[0].second.find { c ->
                    it.value[1].second.contains(c) and it.value[2].second.contains(c)
                }
            }.sumOf {
                getPriority(it!!)
            }
    }

    val input = readInput("Day03")
    input.forEach { check(it.count() % 2 == 0) }
    check(input.count() % 3 == 0)
    println(part1(input))
    println(part2(input))
}
