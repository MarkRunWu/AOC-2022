fun main() {
    fun calculateScore(shape: Pair<Int, Int>): Int {
        val diff = shape.second - shape.first
        return shape.second + (if (diff == 0) {
            3
        } else if ((diff == -2) or (diff == 1)) { // -2: rock - scissors, 1: paper - rock / scissors - paper
            6
        } else {
            0
        })
    }

    fun part1(input: List<String>): Int {
        return input.map { it ->
            val p = it.split(" ").map {
                if ((it == "X") or (it == "A")) {
                    1 // rock
                } else if ((it == "Y") or (it == "B")) {
                    2 // paper
                } else {
                    3 // scissors
                }
            }
            Pair(p.first(), p.last())
        }.sumOf { calculateScore(it) }
    }

    fun part2(input: List<String>): Int {
        return input.map { it ->
            val p = it.split(" ").map {
                if ((it == "X") or (it == "A")) {
                    1 // rock / loss
                } else if ((it == "Y") or (it == "B")) {
                    2 // paper / draw
                } else {
                    3 // scissors / win
                }
            }
            Pair(
                p.first(), when (val q = p.last()) {
                    1 -> (3 + (p.first() - 1) - 1) % 3 + 1;
                    2 -> p.first();
                    3 -> ((p.first() - 1) + 1) % 3 + 1;
                    else -> throw IllegalArgumentException("Invalid value range $q");
                }
            )
        }.sumOf { calculateScore(it) }
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
