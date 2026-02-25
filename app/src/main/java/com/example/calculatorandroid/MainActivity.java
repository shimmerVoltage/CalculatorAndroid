package com.example.calculatorandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private boolean isNewOperation = true;
    private boolean isDecimalAdded = false;

    private static final double MAX_VALUE = Double.MAX_VALUE;
    private static final double MIN_VALUE = -Double.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.textViewDisplay);

        findViewById(R.id.btn_0).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_1).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_2).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_3).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_4).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_5).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_6).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_7).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_8).setOnClickListener(this::onDigitClick);
        findViewById(R.id.btn_9).setOnClickListener(this::onDigitClick);

        findViewById(R.id.btn_plus).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_minus).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_multiply).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_divide).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.btn_percent).setOnClickListener(this::onOperatorClick);

        findViewById(R.id.btn_equals).setOnClickListener(this::onEqualsClick);
        findViewById(R.id.btn_clear).setOnClickListener(this::onClearClick);
        findViewById(R.id.btn_dot).setOnClickListener(this::onDotClick);
        findViewById(R.id.btn_plus_minus).setOnClickListener(this::onPlusMinusClick);
    }

    private void onDigitClick(View view) {
        Button button = (Button) view;
        String digit = button.getText().toString();
        String currentText = display.getText().toString();

        if (isNewOperation) {
            currentText = "";
            isNewOperation = false;
        }

        if (currentText.equals("0") && !digit.equals("0")) {
            currentText = "";
        }

        String newText = currentText + digit;
        if (isNumberTooLarge(newText)) {
            showError("Number is too fat!");
            return;
        }

        display.setText(newText);
    }

    private boolean isNumberTooLarge(String numberStr) {
        try {
            double number = Double.parseDouble(numberStr);
            if (Double.isInfinite(number)) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private void onOperatorClick(View view) {
        Button button = (Button) view;
        String newOperator = button.getText().toString();

        if (!isNewOperation && !display.getText().toString().isEmpty()) {
            if (!operator.isEmpty()) {
                if (!calculate()) {
                    return;
                }
            } else {
                String currentText = display.getText().toString();
                if (isNumberTooLarge(currentText)) {
                    showError("Number is too fat!");
                    return;
                }
                firstNumber = Double.parseDouble(currentText);
            }
        }

        operator = newOperator;
        isNewOperation = true;
        isDecimalAdded = false;
    }

    private void onEqualsClick(View view) {
        if (!operator.isEmpty() && !isNewOperation) {
            calculate();
            operator = "";
            isNewOperation = true;
        }
    }

    private boolean calculate() {
        if (operator.isEmpty()) return false;

        String currentText = display.getText().toString();
        if (currentText.isEmpty()) return false;

        try {
            secondNumber = Double.parseDouble(currentText);

            if (Double.isInfinite(firstNumber) || Double.isInfinite(secondNumber)) {
                showError("Too fat!");
                return false;
            }

            double result = 0;
            boolean overflow = false;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    overflow = Double.isInfinite(result) ||
                            result > MAX_VALUE ||
                            result < MIN_VALUE;
                    break;

                case "−":
                    result = firstNumber - secondNumber;
                    overflow = Double.isInfinite(result) ||
                            result > MAX_VALUE ||
                            result < MIN_VALUE;
                    break;

                case "×":
                    result = firstNumber * secondNumber;
                    overflow = Double.isInfinite(result) ||
                            result > MAX_VALUE ||
                            result < MIN_VALUE;
                    break;

                case "÷":
                    if (secondNumber == 0) {
                        showError("Divided by zero!");
                        return false;
                    }
                    result = firstNumber / secondNumber;
                    overflow = Double.isInfinite(result) ||
                            result > MAX_VALUE ||
                            result < MIN_VALUE;
                    break;

                case "%":
                    result = firstNumber * secondNumber / 100;
                    overflow = Double.isInfinite(result) ||
                            result > MAX_VALUE ||
                            result < MIN_VALUE;
                    break;
            }

            if (overflow) {
                showError("Too fat!");
                return false;
            }

            String resultString = formatResult(result);
            display.setText(resultString);
            firstNumber = Double.parseDouble(resultString);
            return true;

        } catch (NumberFormatException e) {
            showError("Invalid format!");
            return false;
        }
    }

    private String formatResult(double number) {
        if (Double.isNaN(number)) {
            return "Error!";
        }
        if (Double.isInfinite(number)) {
            return "Infinity!";
        }

        if (Math.abs(number) > 1e12) {
            return String.format("%.2e", number);
        }

        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            String str = String.format("%.10f", number);
            str = str.replaceAll("0*$", "").replaceAll("\\.$", "");
            return str;
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        display.setText("Error!");
        firstNumber = 0;
        secondNumber = 0;
        operator = "";
        isNewOperation = true;
        isDecimalAdded = false;
    }

    private void onClearClick(View view) {
        display.setText("0");
        firstNumber = 0;
        secondNumber = 0;
        operator = "";
        isNewOperation = true;
        isDecimalAdded = false;
    }

    private void onDotClick(View view) {
        String currentText = display.getText().toString();

        if (currentText.equals("Error!")) {
            onClearClick(view);
            currentText = "0";
        }

        if (isNewOperation) {
            currentText = "0";
            isNewOperation = false;
        }

        if (!currentText.contains(".")) {
            display.setText(currentText + ".");
            isDecimalAdded = true;
        }
    }

    private void onPlusMinusClick(View view) {
        String currentText = display.getText().toString();
        if (!currentText.equals("0") && !currentText.isEmpty() && !currentText.equals("Error!")) {
            try {
                double number = Double.parseDouble(currentText);

                if (Double.isInfinite(-number)) {
                    showError("Too fat!");
                    return;
                }

                number = -number;
                display.setText(formatResult(number));
            } catch (NumberFormatException e) {
                showError("Error!");
            }
        }
    }
}