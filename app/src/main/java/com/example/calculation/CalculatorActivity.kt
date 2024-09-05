package com.example.calculation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CalculatorActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var input: String = ""
    private var decimalCount: Int = 0
    private var firstNumber: Double? = null
    private var secondNumber: Double? = null
    private var currentOperation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        display = findViewById(R.id.display)

        savedInstanceState?.let {
            input = it.getString("input_key", "")
            decimalCount = it.getInt("decimal_count_key", 0)
            display.text = input
        }
        setNumberButtonListeners()
        setOperatorButtonListeners()
        setClearButtonListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("input_key", input)
        outState.putInt("decimal_count_key", decimalCount)
    }

    private fun setNumberButtonListeners() {
        val numberButtons = listOf<Button>(
            findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
            findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
            findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9),
            findViewById(R.id.button0), findViewById(R.id.buttonDecimal)
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                addNumberToInput(button.text.toString())
            }
        }
    }

    private fun setOperatorButtonListeners() {
        val operators = listOf<Button>(
            findViewById(R.id.buttonPlus), findViewById(R.id.buttonMinus),
            findViewById(R.id.buttonMultiply), findViewById(R.id.buttonDivide),
            findViewById(R.id.buttonEquals)
        )

        for (button in operators) {
            button.setOnClickListener {
                performOperation(button.text.toString())
            }
        }
    }

    private fun setClearButtonListener() {
        val clearButton = findViewById<Button>(R.id.buttonClear)
        clearButton.setOnClickListener {
            clearInput()
        }
    }

    private fun addNumberToInput(value: String) {
        if (value == ".") {
            if (input.contains(".")) {
                return
            }
            decimalCount = 0 // Reset decimal count on new decimal point
        } else if (input.contains(".")) {
            decimalCount++
        }

        if (decimalCount <= 2) {
            input += value
            display.text = input
        }
    }

    private fun clearInput() {
        input = ""
        decimalCount = 0
        display.text = "0"
        firstNumber = null
        secondNumber = null
        currentOperation = null
    }

    private fun performOperation(operation: String) {
        if (operation == "=") {
            secondNumber = input.toDoubleOrNull()

            if (firstNumber != null && secondNumber != null) {
                val result = when (currentOperation) {
                    "+" -> firstNumber!! + secondNumber!!
                    "-" -> firstNumber!! - secondNumber!!
                    "*" -> firstNumber!! * secondNumber!!
                    "/" -> if (secondNumber != 0.0) firstNumber!! / secondNumber!! else {
                        display.text = "Error"
                        return
                    }
                    else -> null
                }

                if (result != null) {
                    val formattedResult = String.format("%.2f", result)
                    display.text = formattedResult
                    input = formattedResult
                    decimalCount = formattedResult.split(".").getOrNull(1)?.length ?: 0
                    firstNumber = null
                    secondNumber = null
                    currentOperation = null
                }
            }
        } else {
            firstNumber = input.toDoubleOrNull()
            input = ""
            currentOperation = operation
        }
    }
}
