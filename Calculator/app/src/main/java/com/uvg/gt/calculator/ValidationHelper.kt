package com.uvg.gt.calculator

class ValidationHelper {

    companion object {
        fun isConvertibleToNumber(element: ExpressionElement): Boolean {
            val elementToString = element.toString()
            var isConvertible = true
            isConvertible = try {
                elementToString.toInt()
                true
            } catch (e1: NumberFormatException) {
                try {
                    elementToString.toFloat()
                    true
                } catch (e2: NumberFormatException) {
                    false
                }
            }
            return isConvertible
        }
        fun isNumberValid(element: ExpressionElement): Boolean {
            val dotCount = element.toString().count { it == '.' }
            return dotCount <= 1
        }

        fun isValidParenthesis(stack: MutableList<ExpressionElement>, index: Int = 0): Boolean {
            val current = stack.elementAt(index)

            if (current == ExpressionElement.OPEN_PARENTHESIS) {
                var nestedCount = 0
                var i = index + 1

                while (i < stack.size) {
                    if (stack.elementAt(i) == ExpressionElement.OPEN_PARENTHESIS) {
                        nestedCount++
                    } else if (stack.elementAt(i) == ExpressionElement.CLOSE_PARENTHESIS) {
                        if (nestedCount == 0) {

                            val updatedStack = stack.toMutableList()

                            updatedStack.removeAt(i)
                            updatedStack.removeAt(index)
                            return isValidParenthesis(updatedStack.toCollection(ArrayDeque()), index)
                        } else {
                            nestedCount--
                        }
                    }
                    i++
                }
            }
            if(current === ExpressionElement.CLOSE_PARENTHESIS){
                return false
            }

            return isValidParenthesis(stack, index + 1)
        }

        fun hasValidOperators(inputList: MutableList<ExpressionElement>): Boolean {
            var validOperators = false
            for (i in 1 until inputList.size) {
                val currentElement = inputList[i]
                val isNumberPrevElement = inputList[i - 1] === ExpressionElement.NUMERO
                val isNumberNextElement = inputList[i + 1] === ExpressionElement.NUMERO

                val isCloseParenthesisPrevElement = inputList[i - 1] === ExpressionElement.CLOSE_PARENTHESIS
                val isOpenParenthesisNextElement = inputList[i + 1] === ExpressionElement.OPEN_PARENTHESIS

                if(isOperator(currentElement) && (
                    (!isNumberPrevElement && !isNumberNextElement) ||
                    (!isCloseParenthesisPrevElement && !isOpenParenthesisNextElement) ||
                    (!isCloseParenthesisPrevElement && !isNumberNextElement) ||
                    (!isNumberPrevElement && !isOpenParenthesisNextElement)
                )) {
                    validOperators = true
                    break
                }
            }
            return validOperators
        }
    }
}