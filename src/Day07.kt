import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

fun main() {

    data class File(val name: String, val size: Int)
    data class Directory(
        val name: String,
        val parent: Directory?,
        val files: ArrayList<File> = arrayListOf(),
        val childDirectory: ArrayList<Directory> = arrayListOf(),
        var calculatedSize: Int = 0
    )

    fun parseDirectory(input: List<String>): Directory {
        var curIndex = 0
        val root = Directory("/", null)
        var currentDirectory: Directory? = null
        while (curIndex < input.size) {
            val curLine = input[curIndex]

            if (curLine.startsWith("$")) {
                val texts = curLine.split(" ")
                when (texts[1]) {
                    "cd" -> {
                        currentDirectory = when (val dir = texts[2]) {
                            "/" -> root
                            ".." -> currentDirectory?.parent
                            else -> {
                                currentDirectory?.childDirectory?.find { it.name == dir }
                            }
                        }
                        curIndex++
                    }
                    "ls" -> {
                        if (currentDirectory == null) {
                            throw IllegalStateException("Directory is not available")
                        }
                        var lsIndex = curIndex + 1
                        while (lsIndex < input.size && !input[lsIndex].startsWith("$")) {
                            val lineTexts = input[lsIndex].split(" ")
                            when (lineTexts[0]) {
                                "dir" -> currentDirectory.childDirectory.add(
                                    Directory(lineTexts[1], currentDirectory)
                                )
                                else -> {
                                    currentDirectory.files.add(
                                        File(
                                            lineTexts[1],
                                            lineTexts[0].toInt()
                                        )
                                    )
                                }
                            }
                            lsIndex++
                        }
                        curIndex = lsIndex
                    }
                }
            }
        }
        return root
    }

    fun calculateDirectorySize(dir: Directory) {
        dir.calculatedSize = dir.childDirectory.sumOf {
            calculateDirectorySize(it)
            it.calculatedSize
        } + dir.files.sumOf { it.size }
    }

    fun findDirs(dir: Directory, filter: (Int) -> Boolean): List<Directory> {
        val queue = LinkedList<Directory>()
        queue.addFirst(dir)
        val qualifiedDirs = ArrayList<Directory>()
        while (queue.isNotEmpty()) {
            val d = queue.poll()
            if (filter(d.calculatedSize)) {
                qualifiedDirs.add(d)
            }
            queue.addAll(d.childDirectory)
        }
        return qualifiedDirs
    }

    fun part1(input: List<String>): Int {
        val dir: Directory = parseDirectory(input)
        calculateDirectorySize(dir)
        return findDirs(dir) { size -> size < 100000 }.sumOf { it.calculatedSize }
    }

    fun part2(input: List<String>): Int {
        val dir = parseDirectory(input)
        calculateDirectorySize(dir)
        val goal = 30000000 - (70000000 - dir.calculatedSize)
        return findDirs(dir) { size -> size > goal }
            .minOf { it.calculatedSize }
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
