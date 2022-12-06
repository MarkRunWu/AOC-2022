import java.lang.Math.abs
import java.util.Stack

fun main() {

    fun parseStacks(input: List<String>): List<Stack<String>> {
        val stackStrings = input.joinToString("\n") { it }.split(
            "\n\n"
        )[0].split("\n")
        val labelLine = stackStrings.last().split("   ")
        val stacks = labelLine.map {
            Stack<String>()
        }
        val stackSize = stacks.size
        val stackValues = stackStrings.subList(0, stackStrings.size - 1).reversed()
            .map {
                val items = arrayListOf<String>()
                for (i in 0 until stackSize) {
                    if (i * 4 + 3 <= it.length) {
                        items.add(it.slice(i * 4 until i * 4 + 3).trim())
                    } else {
                        items.add("")
                    }
                }
                items
            }
        for (values in stackValues) {
            for (i in 0 until stackSize) {
                if (values[i].isEmpty()) {
                    continue
                }
                stacks[i].push(values[i])
            }
        }
        return stacks
    }

    data class Movement(val steps: Int, val fromIndex: Int, val toIndex: Int)

    fun parseMovementList(commands: List<String>): List<Movement> {
        val pattern = Regex("^move (\\d+) from (\\d+) to (\\d+)")
        return commands.map {
            pattern.findAll(it)
        }.map {
            Movement(
                it.first().groups[1]!!.value.toInt(),
                it.first().groups[2]!!.value.toInt() - 1,
                it.first().groups[3]!!.value.toInt() - 1
            )
        }
    }

    fun part1(input: List<String>): String {
        val stacks = parseStacks(input)
        val movementList = parseMovementList(input.joinToString("\n") { it }.split("\n\n").last().split("\n"))
        movementList.forEach {
            for (i in 0 until it.steps) {
                stacks[it.toIndex].push(stacks[it.fromIndex].pop())
            }
        }
        return stacks.map {
            it.last()
        }.joinToString("") { it }.replace("[", "").replace("]", "")
    }

    fun part2(input: List<String>): String {
        val stacks = parseStacks(input)
        val movementList = parseMovementList(input.joinToString("\n") { it }.split("\n\n").last().split("\n"))
        val tmpStack = Stack<String>()
        movementList.forEach {
            tmpStack.clear()
            for (i in 0 until it.steps) {
                tmpStack.push(stacks[it.fromIndex].pop())
            }
            for (i in 0 until it.steps) {
                stacks[it.toIndex].push(tmpStack.pop())
            }
        }
        return stacks.map {
            it.last()
        }.joinToString("") { it }.replace("[", "").replace("]", "")
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
