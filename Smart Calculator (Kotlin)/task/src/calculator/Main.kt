package calculator

import java.util.*

fun cleanupRedundantSigns(input: MutableList<String>): MutableList<String> {
    for (i in 0 until input.lastIndex) {
        val test = input[i] + input[i + 1]
        when {
            test.matches(Regex("\\+-.*")) -> {
                input[i] = "-"
                input[i + 1] = input[i + 1].drop(1)
            }
            test.matches(Regex("--.*")) -> {
                input[i] = "+"
                input[i + 1] = input[i + 1].drop(1)
            }
        }
    }
    return input
}

fun infixToPostfix(input: MutableList<String>): MutableList<String> {
    val queue: Queue<String> = LinkedList()
    val stack: Stack<String> = Stack()
    val postfix: MutableList<String> = mutableListOf()
    for (element in input) {
        //println("STACK: $stack")
        //println("QUEUE: $queue")
        when {
            element.matches(Regex("[0-9]*+")) -> {
                queue.add(element)
                //println("%")
            }
            element.matches(Regex("[+\\-*/]")) -> {
                if (stack.isEmpty() || stack.last() == "(") {
                    //println("1")
                    stack.push(element)
                } else if (stack.last().matches(Regex("[+-]")) && element.matches(Regex("[*/]"))) {
                    //println("2")
                    stack.push(element)
                } else if (stack.last().matches(Regex("[*/]")) && element.matches(Regex("[+-]"))) {
                    //println("3")
                    queue.addAll(pop(stack))
                    stack.push(element)
                } else {
                    //println("4")
                    queue.addAll(pop(stack))
                    stack.push(element)
                }
            }
            element == "(" -> stack.push(element)
            element == ")" -> {
                if (input.contains("(")) {
                    queue.addAll(pop(stack))
                }
            }
        }

    }
    queue.addAll(stack)
//    println("STACK po: $stack")
//    println("QUEUE po: $queue")
    queue.forEach {
        postfix.add(it)
    }
    return postfix
}

fun pop(stack: Stack<String>): List<String> {
    val que: MutableList<String> = mutableListOf()
    for (i in stack.lastIndex downTo 0) {
        if (stack[i] == "(") {
            stack[i] = " "
            continue
        }
        que.add(stack[i])
        stack[i] = " "
    }
    stack.removeIf { it == " " }
    return que
}

fun postFixToResult(input: MutableList<String>): Int {
    val stack: Stack<Int> = Stack()
    for (element in input) {
        when {
            element.matches(Regex("[0-9]*+")) -> stack.push(element.toInt())
            element.matches(Regex("\\+")) -> {
                stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] + stack.last()
                stack.pop()
            }
            element.matches(Regex("-")) -> {
                stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] - stack.last()
                stack.pop()
            }
            element.matches(Regex("\\*")) -> {
                stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] * stack.last()
                stack.pop()
            }
            element.matches(Regex("/")) -> {
                stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] / stack.last()
                stack.pop()
            }
        }
    }
    return stack.last()
}

fun removeSlice(list: MutableList<String>, from: Int, end: Int) {
    for (i in end downTo from) {
        list.removeAt(i)
    }
}

fun main() {
    val variableMap = mutableMapOf<String, String>()

    var score: Int
    while (true) {
        val input = readln().replace("\\s+".toRegex(), " ").trim()
        if (input.isEmpty()) continue
        if (input == "/exit") break
        if (input == "/help") {
            println("The program calculates the sum of numbers")
            continue
        }
        if (input.contains("(") && !input.contains(")")) {
            println("Invalid expression")
            continue
        } else if (!input.contains("(") && input.contains(")")) {
            println("Invalid expression")
            continue
        }
        if (input.matches(Regex("/[!-z]*"))) {
            println("Unknown command")
            continue
        }
        if (input.matches(Regex("[A-Za-z]*")) && !variableMap.contains(input)) {
            println("Unknown variable")
            continue
        }
        if (input.contains("=")) {
            val variables = input.replace("\\s+".toRegex(), "").split("=").toMutableList()
            if (!variables[0].matches(Regex("[A-Za-z]*"))) {
                println("Invalid identifier")
                continue
            } else if (variables[1].matches(Regex(".*[A-Za-z]+[0-9].*|.*[0-9]+[A-Za-z].*")) || variables.size > 2) {
                println("Invalid assignment")
                continue
            } else if (variables[1].matches(Regex("[a-zA-z]")) && !variableMap.contains(variables[1])) {
                println("Unknown variable")
                continue
            }
            if (variableMap.contains(variables[1])) {
                variableMap[variables[0]] = variableMap[variables[1]]!!
            } else {
                variableMap[variables[0]] = variables[1]
            }
            continue
        }
        val operation = input.split(" ").toMutableList()
        val test: MutableList<String> = mutableListOf()
        for (i in 0 until operation.size) {
            //println(operation)
            when {
                operation[i].matches(Regex("--")) || operation[i].matches(Regex("----")) -> operation[i] = "+"
                operation[i].matches(Regex("-\\+")) || operation[i].matches(Regex("---")) || operation[i].matches(
                    Regex(
                        "\\+-"
                    )
                ) -> operation[i] = "-"
                operation[i].matches(Regex("\\++")) -> operation[i] = "+"
                variableMap.contains(operation[i]) -> operation[i] = variableMap[operation[i]].toString()
            }
        }
        //println(operation)
        cleanupRedundantSigns(operation)
        try {
            if (operation.size == 1){
                score = operation[0].toInt()
                println(score)
                continue
            }
        } catch (_: NumberFormatException) {}
        try {
            score = postFixToResult(infixToPostfix(operation))
        } catch (e: ArrayIndexOutOfBoundsException) {
            println(e.message)
            println("Invalid expression")
            continue
        }
        println(score)
    }
    println("Bye!")
}
