import java.lang.Math.abs

fun main() {
    fun parseSectionsAssignments(input: List<String>): List<List<Pair<Int, Int>>> {
        return input.map { it ->
            it.split(',')
                .map {
                    val pairs = it.split('-')
                    Pair(pairs[0].toInt(), pairs[1].toInt())
                }
        }
    }

    fun part1(input: List<String>): Int {
        return parseSectionsAssignments(input)
            .map { it -> it.sortedBy { it.first } }
            .count {
                (it[0].second >= it[1].second) or (
                        (it[0].first == it[1].first) and (it[0].second < it[1].second))

            }
    }

    fun part2(input: List<String>): Int {
        return parseSectionsAssignments(input)
            .map { it -> it.sortedBy { it.first } }
            .count {
                it[0].second >= it[1].first
            }
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
