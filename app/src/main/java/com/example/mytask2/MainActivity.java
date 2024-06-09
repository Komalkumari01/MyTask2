package com.example.mytask2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result_tv, sol_tv;
    MaterialButton C, open_b, close_b;
    MaterialButton divide, multiply, subtract, add, equal;
    MaterialButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    MaterialButton AC, dot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        result_tv = findViewById(R.id.result_tv);
        sol_tv = findViewById(R.id.sol_tv);

        // Initialize Buttons
        assignId(C, R.id.C);
        assignId(open_b, R.id.open_b);
        assignId(close_b, R.id.close_b);
        assignId(divide, R.id.divide);
        assignId(multiply, R.id.multiply);
        assignId(subtract, R.id.subtract);
        assignId(add, R.id.add);
        assignId(equal, R.id.equal);
        assignId(btn0, R.id.btn0);
        assignId(btn1, R.id.btn1);
        assignId(btn2, R.id.btn2);
        assignId(btn3, R.id.btn3);
        assignId(btn4, R.id.btn4);
        assignId(btn5, R.id.btn5);
        assignId(btn6, R.id.btn6);
        assignId(btn7, R.id.btn7);
        assignId(btn8, R.id.btn8);
        assignId(btn9, R.id.btn9);
        assignId(AC, R.id.AC);
        assignId(dot, R.id.dot);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String calculate = sol_tv.getText().toString();

        if (buttonText.equals("AC")) {
            // Clear all
            sol_tv.setText("");
            result_tv.setText("0");
        } else if (buttonText.equals("=")) {
            // Calculate the result
            String result = getResult(calculate);
            sol_tv.setText(result);
            result_tv.setText(result);
        } else if (buttonText.equals("C")) {
            // Delete the last character
            if (!calculate.isEmpty()) {
                calculate = calculate.substring(0, calculate.length() - 1);
            }
            sol_tv.setText(calculate);
        } else if (buttonText.equals(".")) {
            // Append decimal point if not already present and if the expression is not empty
            if (!calculate.contains(".") && !calculate.isEmpty()) {
                calculate += buttonText;
                sol_tv.setText(calculate);
            }
        } else {
            // Append the clicked button's text to the expression
            calculate += buttonText;
            sol_tv.setText(calculate);
        }
    }

    String getResult(String data) {
        try {
            // Evaluate the expression
            double result = evaluateExpression(data);
            // Convert the result to a string
            return String.valueOf(result);
        } catch (Exception e) {
            return "Error";
        }
    }

    double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s", ""); // Remove spaces
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        StringBuilder operandBuilder = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                operandBuilder.append(c);
            } else {
                if (operandBuilder.length() > 0) {
                    numbers.push(Double.parseDouble(operandBuilder.toString()));
                    operandBuilder.setLength(0); // Clear the operand builder
                }
                if (c == '(') {
                    operators.push(c);
                } else if (c == ')') {
                    while (operators.peek() != '(') {
                        double result = applyOperator(numbers.pop(), numbers.pop(), operators.pop());
                        numbers.push(result);
                    }
                    operators.pop(); // Discard the '('
                } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                    while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                        double result = applyOperator(numbers.pop(), numbers.pop(), operators.pop());
                        numbers.push(result);
                    }
                    operators.push(c);
                }
            }
        }

        if (operandBuilder.length() > 0) {
            numbers.push(Double.parseDouble(operandBuilder.toString()));
        }

        while (!operators.isEmpty()) {
            double result = applyOperator(numbers.pop(), numbers.pop(), operators.pop());
            numbers.push(result);
        }

        return numbers.pop();
    }

    private double applyOperator(double b, double a, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }
        return 0;
    }

    private int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0;
    }
}
