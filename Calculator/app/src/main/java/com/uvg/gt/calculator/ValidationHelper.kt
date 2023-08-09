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

        fun isValidParenthesis(stack: ArrayDeque<ExpressionElement>, index: Int = 0): Boolean {
            if (index >= stack.size) {
                return true
            }

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

            return isValidParenthesis(stack, index + 1)
        }

        fun hasValidOperators(inputList: MutableList<ExpressionElement>): Boolean {
            var invalidOperatorFound = false
            for (i in 1 until inputList.size) {
                val currentElement = inputList[i]
                val prevElement = inputList[i - 1]
                val nextElement = inputList[i + 1]

                if(isOperator(currentElement) && (!isConvertibleToNumber(prevElement) || !isConvertibleToNumber(nextElement))) {
                    invalidOperatorFound = true
                    break
                }
            }
            return invalidOperatorFound
        }
    }
}