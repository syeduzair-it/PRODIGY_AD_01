package com.example.uzcalc;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    EditText display;
    StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        setNumberListeners();
        setOperatorListeners();
    }

    // Handles number and dot buttons
    private void setNumberListeners() {
        int[] numIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btn00, R.id.btnDot
        };

        for (int id : numIds) {
            MaterialButton btn = findViewById(id);
            btn.setOnClickListener(v -> {
                expression.append(btn.getText());
                display.setText(expression.toString());
                showLiveResult();
            });
        }
    }

    // Handles operators, equal, delete, and clear
    private void setOperatorListeners() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> addOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> addOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> addOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> addOperator("/"));
        findViewById(R.id.btnMod).setOnClickListener(v -> addOperator("%"));

        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            String result = evaluate(expression.toString());
            display.setText(result);
            expression = new StringBuilder(result); // Allow continuing from result
        });

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            expression.setLength(0);
            display.setText("0");
        });

        findViewById(R.id.btnDel).setOnClickListener(v -> {
            int len = expression.length();
            if (len > 0) {
                expression.deleteCharAt(len - 1);
                if (expression.length() == 0) {
                    display.setText("0");
                } else {
                    display.setText(expression.toString());
                    showLiveResult();
                }
            }
        });
    }

    // Adds operator only if last character is not another operator
    private void addOperator(String op) {
        if (expression.length() > 0) {
            char last = expression.charAt(expression.length() - 1);
            if ("+-*/.%".indexOf(last) == -1) {
                expression.append(op);
                display.setText(expression.toString());
            }
        }
    }

    // Shows live calculation preview
    private void showLiveResult() {
        String result = evaluate(expression.toString());
        if (!result.equals("Error")) {
            display.setText(expression + "\n= " + result);
        }
    }

    // Safely evaluates the math expression using Exp4j
    private String evaluate(String expr) {
        try {
            Expression e = new ExpressionBuilder(expr).build();
            double result = e.evaluate();

            if (result == (long) result)
                return String.valueOf((long) result);
            else
                return String.valueOf(result);
        } catch (Exception e) {
            return "Error";
        }
    }
}
