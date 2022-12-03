fun main() {
    fun getElfCalories(input: List<String>): List<Int> {
        return input.joinToString(separator = "\n").split("\n\n").map { it ->
            it.split("\n").sumOf { it.toInt() }
        }
    }

    fun part1(input: List<String>): Int {
        return getElfCalories(input).maxOf { it }
    }

    fun part2(input: List<String>): Int {
        return getElfCalories(input).sortedDescending().take(3).sum()
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
