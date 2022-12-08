fun main() {
    fun findFirstMarkerIndex(message: String, markerLength: Int): Int {
        var start = 0
        var end = 1
        while (end < message.length) {
            for (i in start until end) {
                if (message[i] == message[end]) {
                    start = i + 1
                    break
                }
            }
            if (end - start + 1 == markerLength) {
                return end
            }
            end++
        }
        return -1
    }

    fun part1(input: List<String>): List<Int> {
        return input.map {
            findFirstMarkerIndex(it, 4)
        }.map { it + 1 }
    }

    fun part2(input: List<String>): List<Int> {
        return input.map {
            findFirstMarkerIndex(it, 14)
        }.map { it + 1 }
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
