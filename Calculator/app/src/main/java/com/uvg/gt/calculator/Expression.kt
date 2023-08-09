package com.uvg.gt.calculator

import java.io.InvalidObjectException
import java.util.LinkedList
import java.util.Queue
import java.util.Stack
import kotlin.math.pow


data class Expression (
    val tokens: MutableList<ExpressionElement>,
    val numbers: MutableList<Float>,
)

fun evaluate(exp: Expression): Float {
    var postfix = infixToPostfix(exp);
    val stack: Stack<Float> = Stack()

    for (element in postfix) {
        if (element is Float) {
            stack.push(element)
        } else if (element is ExpressionElement) {
            val y = stack.pop()
            val x = stack.pop()

            when (element) {
                ExpressionElement.MAS -> stack.push(x+y)
                ExpressionElement.MENOS -> stack.push(x-y)
                ExpressionElement.MULTI -> stack.push(x*y)
                ExpressionElement.DIV -> stack.push(x/y)
                ExpressionElement.MOD -> stack.push(x%y)
                ExpressionElement.POTENCIA -> stack.push(x.pow(y))
                else -> throw InvalidObjectException("the element can't be of type number!")
            }
        }
    }
    return stack.pop()
}

private fun infixToPostfix(exp: Expression): Queue<Any> {
    val ans: Queue<Any> = LinkedList()
    val stack: Stack<ExpressionElement> = Stack()

    var numberIndex = 0
    val len = exp.tokens.count() - 1

    for (i in 0..len step 1) {

        // If the scanned character is operand
        // add it to the postfix expression
        if (exp.tokens[i] == ExpressionElement.NUMERO) {
            ans.add(exp.numbers[numberIndex++])
        }

        // if the scanned character is '('
        // push it in the stack
        else if (exp.tokens[i] == ExpressionElement.OPEN_PARENTHESIS) {
            stack.push(exp.tokens[i])
        }

        // if the scanned character is ')'
        // pop the stack and add it to the
        // output string until empty or '(' found
        else if (exp.tokens[i] == ExpressionElement.CLOSE_PARENTHESIS) {
            while (stack.isNotEmpty() && stack.peek() != ExpressionElement.OPEN_PARENTHESIS)
                ans.add(stack.pop())
        }

        // If the scanned character is an operator
        // push it in the stack
        else if (isOperator(exp.tokens[i])) {
            while (stack.isNotEmpty() && precedence(stack.peek()) >= precedence(exp.tokens[i])){
                ans.add(stack.pop())
            }
            stack.push(exp.tokens[i])
        }
    }

    while (stack.isNotEmpty()) {
        ans.add(stack.pop())
    }
    return ans
}

fun isOperator(el: ExpressionElement): Boolean {
    return when (el) {
        ExpressionElement.NUMERO -> false
        ExpressionElement.MAS -> true
        ExpressionElement.MENOS -> true
        ExpressionElement.MULTI -> true
        ExpressionElement.DIV -> true
        ExpressionElement.MOD -> true
        ExpressionElement.POTENCIA -> true
        ExpressionElement.OPEN_PARENTHESIS -> false
        ExpressionElement.CLOSE_PARENTHESIS -> false
    }
}

private fun precedence(op: ExpressionElement): Int {
    return when(op) {
        ExpressionElement.MAS -> 1
        ExpressionElement.MENOS -> 1
        ExpressionElement.MULTI -> 2
        ExpressionElement.DIV -> 2
        ExpressionElement.MOD -> 3
        ExpressionElement.POTENCIA -> 3
        else -> -1
    }
}