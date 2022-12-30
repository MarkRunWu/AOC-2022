data class Monkey(
    val items: ArrayList<UInt>,
    val operation: (UInt) -> UInt,
    val test: (UInt) -> Int,
    var inspectItemCount: UInt = 0u
) {
    companion object {
        fun parse(texts: List<String>): Monkey {
            if (texts[0].startsWith("Monkey")) {
                val items = ArrayList<UInt>(texts[1].split(":").last().split(",").map { it.trim().toUInt() })
                val formulate = texts[2].split(": new = ").last()
                val tokens = formulate.split(" ")
                val operation = { item: UInt ->
                    val item1 = if (tokens[0] == "old") item else tokens[0].toUInt()
                    val item2 = if (tokens[2] == "old") item else tokens[2].toUInt()
                    if (tokens[1] == "*") {
                        item1 * item2
                    } else {
                        item1 + item2
                    }
                }
                val testFormulate = texts[3].split(":").last()
                val divider = testFormulate.split("divisible by").last().trim().toUInt()
                val monkeyIndexWhenTrue = texts[4].split(":").last().split("throw to monkey").last().trim().toInt()
                val monkeyIndexWhenFalse = texts[5].split(":").last().split("throw to monkey").last().trim().toInt()
                val test = { item: UInt ->
                    if (item % divider == 0u) monkeyIndexWhenTrue else monkeyIndexWhenFalse
                }
                return Monkey(items, operation, test)
            }
            throw IllegalStateException("Failed to parse input as Monkey data class")
        }
    }
}

fun main() {
    fun parseMonkeys(input: List<String>): List<Monkey> {
        return input.joinToString("\n") { it }.split("\n\n").map {
            Monkey.parse(it.split("\n"))
        }
    }

    fun run(monkeys: List<Monkey>, rounds: Int, boringFactor: Int = 1) {
        var curRound = 1
        while (curRound <= rounds) {
            for (i in monkeys.indices) {
                val curMonkey = monkeys[i]
                curMonkey.inspectItemCount += curMonkey.items.count().toUInt()
                val newItems = curMonkey.items.map { curMonkey.operation(it) / boringFactor.toUInt() }
                curMonkey.items.clear()
                curMonkey.items.addAll(newItems)
                curMonkey.items.forEach { monkeys[curMonkey.test(it)].items.add(it) }
                curMonkey.items.clear()
            }
            curRound++
        }
    }

    fun part1(input: List<String>): UInt {
        val monkeys = parseMonkeys(input)
        run(monkeys, 20, 3)
        val topInspectedCounts = monkeys.map { it.inspectItemCount }.sortedDescending().take(2)
        return topInspectedCounts[0] * topInspectedCounts[1]
    }

    fun part2(input: List<String>): UInt {
        val monkeys = parseMonkeys(input)
        run(monkeys, 10000)
        val topInspectedCounts = monkeys.map { it.inspectItemCount }.sortedDescending().take(2)
        println(topInspectedCounts)
        return topInspectedCounts[0] * topInspectedCounts[1]
    }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
