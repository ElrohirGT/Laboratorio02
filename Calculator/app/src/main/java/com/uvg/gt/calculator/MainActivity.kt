package com.uvg.gt.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var textField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val equalsButton: Button = findViewById(R.id.button17)
        // onClick del boton =
        equalsButton.setOnClickListener {
            onEqualsButtonClick(it)
        }

        val clear: Button = findViewById(R.id.button20)

        // onClick del boton clear
        clear.setOnClickListener {
            onClearButtonClick(it)
        }

        val borrar: Button = findViewById(R.id.button19)

        // onClick del boton borrar
        borrar.setOnClickListener {
            onBackspaceButtonClick(it)
        }
        textField = findViewById(R.id.textField)

        // Set clicklisteners de todos los botones
        setButtonClickListeners()
    }

    private fun setButtonClickListeners() {
        val buttonClickListener = View.OnClickListener { v ->
            val button = v as Button
            val buttonText = button.text.toString()

            // se pone el texto del boton en el textfield
            textField.append(buttonText)
        }

        // listeners de los números
        findViewById<Button>(R.id.button).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button2).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button3).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button4).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button5).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button6).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button7).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button8).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button9).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button10).setOnClickListener(buttonClickListener)

        // listeners de los símbolos
        findViewById<Button>(R.id.button13).setOnClickListener(buttonClickListener) // *
        findViewById<Button>(R.id.button14).setOnClickListener(buttonClickListener) // /
        findViewById<Button>(R.id.button15).setOnClickListener(buttonClickListener) // %
        findViewById<Button>(R.id.button16).setOnClickListener(buttonClickListener) // ^
        findViewById<Button>(R.id.button11).setOnClickListener(buttonClickListener) // +
        findViewById<Button>(R.id.button12).setOnClickListener(buttonClickListener) // -
        findViewById<Button>(R.id.button18).setOnClickListener(buttonClickListener) // .
        findViewById<Button>(R.id.button21).setOnClickListener(buttonClickListener) // (
        findViewById<Button>(R.id.button22).setOnClickListener(buttonClickListener) // )
    }

    // =
    fun onEqualsButtonClick(view: View) {
        val expressionText = textField.text.toString()

        // Array que guarda los enum de los elementos
        val expressionArray = mutableListOf<ExpressionElement>()
        val expressionNumbers = mutableListOf<Float>()

        val currentNumber = StringBuilder()

        fun addNumberToExpression() {
            if (currentNumber.isNotEmpty()) {
                expressionArray.add(ExpressionElement.NUMERO)
                expressionNumbers.add(currentNumber.toString().toFloat())
                currentNumber.clear()
            }
        }

        for (char in expressionText) {
            when (char) {
                '+' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.MAS)
                }
                '-' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.MENOS)
                }
                '*' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.MULTI)
                }
                '/' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.DIV)
                }
                '%' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.MOD)
                }
                '^' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.POTENCIA)
                }
                '(' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.OPEN_PARENTHESIS)
                }
                ')' -> {
                    addNumberToExpression()
                    expressionArray.add(ExpressionElement.CLOSE_PARENTHESIS)
                }
                in '0'..'9', '.' -> {
                    currentNumber.append(char)
                }
                else -> {
                    // Ignora cualquier otra cosa
                }
            }
        }

        addNumberToExpression()

        if (isValid(expressionArray)) {
            val expression = Expression(expressionArray, expressionNumbers)
            val total = evaluate(expression)
            textField.setText(total.toString())
        }
        else {
            textField.setText("Invalid expression")
        }
    }

    private fun isValid(exp: MutableList<ExpressionElement>): Boolean {
        var validationResult = true
        if(!ValidationHelper.isValidParenthesis(exp)){
            validationResult = false
        }
        if(!ValidationHelper.hasValidOperators(exp)){
            validationResult = false
        }
        return validationResult
    }

    // CLEAR
    fun onClearButtonClick(view: View) {
        textField.text.clear()
    }

    // BORRAR
    fun onBackspaceButtonClick(view: View) {
        val currentText = textField.text.toString()
        if (currentText.isNotEmpty()) {
            textField.text.delete(currentText.length - 1, currentText.length)
        }
    }
}