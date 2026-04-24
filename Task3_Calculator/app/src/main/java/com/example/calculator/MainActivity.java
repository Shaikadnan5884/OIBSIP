package com.example.calculator;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvEquation;
    private TextView tvResult;
    private String equation = "";
    private boolean isNewOp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvEquation = findViewById(R.id.tvEquation);
        tvResult = findViewById(R.id.tvResult);
        
        tvEquation.setText("0");
        tvResult.setText("");
        updateTextSize();
    }

    public void onDigitClick(View view) {
        Button button = (Button) view;
        String digit = button.getText().toString();
        
        if (isNewOp || equation.equals("0")) {
            equation = digit;
            isNewOp = false;
        } else {
            equation += digit;
        }
        tvEquation.setText(equation);
        updateTextSize();
        calculateIntermediateResult();
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String operator = button.getText().toString();
        
        if (!equation.isEmpty()) {
            char lastChar = equation.charAt(equation.length() - 1);
            if (isOperator(String.valueOf(lastChar))) {
                equation = equation.substring(0, equation.length() - 1) + operator;
            } else {
                equation += operator;
            }
            tvEquation.setText(equation);
            updateTextSize();
            isNewOp = false;
        }
    }

    private void updateTextSize() {
        int length = equation.length();
        float size;
        if (length <= 7) {
            size = 56f;
        } else if (length <= 11) {
            size = 40f;
        } else if (length <= 15) {
            size = 30f;
        } else {
            size = 24f;
        }
        tvEquation.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("×") || s.equals("÷") || s.equals("%");
    }

    public void onClearClick(View view) {
        equation = "0";
        tvEquation.setText(equation);
        tvResult.setText("");
        updateTextSize();
        isNewOp = true;
    }

    public void onBackspaceClick(View view) {
        if (!equation.isEmpty() && !equation.equals("0")) {
            equation = equation.substring(0, equation.length() - 1);
            if (equation.isEmpty()) equation = "0";
            tvEquation.setText(equation);
            updateTextSize();
            calculateIntermediateResult();
        }
    }

    private void calculateIntermediateResult() {
        try {
            String tempEq = equation;
            if (tempEq.isEmpty() || tempEq.equals("0")) {
                tvResult.setText("");
                return;
            }
            
            char lastChar = tempEq.charAt(tempEq.length() - 1);
            if (isOperator(String.valueOf(lastChar))) {
                tempEq = tempEq.substring(0, tempEq.length() - 1);
            }
            
            if (tempEq.isEmpty()) {
                tvResult.setText("");
                return;
            }

            double res = evaluate(tempEq);
            
            if (res == (long) res) {
                tvResult.setText(String.valueOf((long) res));
            } else {
                tvResult.setText(String.valueOf(res));
            }
        } catch (Exception e) {
            tvResult.setText("");
        }
    }

    private double evaluate(String expression) {
        final String expr = expression.replace("×", "*").replace("÷", "/");
        
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else if (eat('%')) x = x % parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else {
                    return 0;
                }

                return x;
            }
        }.parse();
    }

    public void onEqualClick(View view) {
        if (!tvResult.getText().toString().isEmpty()) {
            equation = tvResult.getText().toString();
            tvEquation.setText(equation);
            tvResult.setText("");
            updateTextSize();
            isNewOp = true;
        }
    }
}
