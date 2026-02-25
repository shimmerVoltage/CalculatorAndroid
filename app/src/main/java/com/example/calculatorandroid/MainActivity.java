package com.example.calculatorandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        display.setText(currentText + digit);
    }

    private void onOperatorClick(View view) {
        Button button = (Button) view;
        String newOperator = button.getText().toString();

        if (!isNewOperation && !display.getText().toString().isEmpty()) {
            if (!operator.isEmpty()) {
                calculate();
            } else {
                firstNumber = Double.parseDouble(display.getText().toString());
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

    private void calculate() {
        if (operator.isEmpty()) return;

        String currentText = display.getText().toString();
        if (currentText.isEmpty()) return;

        secondNumber = Double.parseDouble(currentText);
        double result = 0;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "−":
                result = firstNumber - secondNumber;
                break;
            case "×":
                result = firstNumber * secondNumber;
                break;
            case "÷":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Ошибка");
                    operator = "";
                    firstNumber = 0;
                    isNewOperation = true;
                    return;
                }
                break;
            case "%":
                result = firstNumber * secondNumber / 100;
                break;
        }

        String resultString = formatResult(result);
        display.setText(resultString);
        firstNumber = Double.parseDouble(resultString);
    }

    private String formatResult(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            return String.valueOf(number);
        }
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
        if (!currentText.equals("0") && !currentText.isEmpty()) {
            double number = Double.parseDouble(currentText);
            number = -number;
            display.setText(formatResult(number));
        }
    }
}