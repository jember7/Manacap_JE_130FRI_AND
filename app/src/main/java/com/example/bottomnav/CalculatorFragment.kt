package com.example.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment(R.layout.activity_calculator) {

    private lateinit var display: TextView
    private var input: String = ""
    private var decimalCount: Int = 0
    private var firstNumber: Double? = null
    private var secondNumber: Double? = null
    private var currentOperation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        display = view.findViewById(R.id.display)

        savedInstanceState?.let {
            input = it.getString("input_key", "")
            decimalCount = it.getInt("decimal_count_key", 0)
            firstNumber = it.getDouble("first_number_key", Double.NaN).takeIf { it != Double.NaN }
            secondNumber = it.getDouble("second_number_key", Double.NaN).takeIf { it != Double.NaN }
            currentOperation = it.getString("current_operation_key", null)
            display.text = input
        }

        setNumberButtonListeners(view)
        setOperatorButtonListeners(view)
        setClearButtonListener(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("input_key", input)
        outState.putInt("decimal_count_key", decimalCount)
        outState.putDouble("first_number_key", firstNumber ?: Double.NaN)
        outState.putDouble("second_number_key", secondNumber ?: Double.NaN)
        outState.putString("current_operation_key", currentOperation)
    }

    private fun setNumberButtonListeners(view: View) {
        val numberButtons = listOf<Button>(
            view.findViewById(R.id.button1), view.findViewById(R.id.button2), view.findViewById(R.id.button3),
            view.findViewById(R.id.button4), view.findViewById(R.id.button5), view.findViewById(R.id.button6),
            view.findViewById(R.id.button7), view.findViewById(R.id.button8), view.findViewById(R.id.button9),
            view.findViewById(R.id.button0), view.findViewById(R.id.buttonDecimal)
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                addNumberToInput(button.text.toString())
            }
        }
    }

    private fun setOperatorButtonListeners(view: View) {
        val operators = listOf<Button>(
            view.findViewById(R.id.buttonPlus), view.findViewById(R.id.buttonMinus),
            view.findViewById(R.id.buttonMultiply), view.findViewById(R.id.buttonDivide),
            view.findViewById(R.id.buttonEquals)
        )

        for (button in operators) {
            button.setOnClickListener {
                performOperation(button.text.toString())
            }
        }
    }

    private fun setClearButtonListener(view: View) {
        val clearButton = view.findViewById<Button>(R.id.buttonClear)
        clearButton.setOnClickListener {
            clearInput()
        }
    }

    private fun addNumberToInput(value: String) {
        if (value == ".") {
            if (input.contains(".")) {
                return
            }
            decimalCount = 0
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
