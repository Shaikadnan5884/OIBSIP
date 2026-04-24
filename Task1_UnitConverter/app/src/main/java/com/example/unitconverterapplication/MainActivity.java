package com.example.unitconverterapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView fromValueText, toValueText;
    private Spinner fromSpinner, toSpinner;
    private String currentCategory;
    private StringBuilder currentInput = new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentCategory = getIntent().getStringExtra("CATEGORY");
        if (currentCategory == null) currentCategory = "Length";

        setupToolbar();

        fromValueText = findViewById(R.id.fromValueText);
        toValueText = findViewById(R.id.toValueText);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);

        setupSpinners();
        setupKeypad();

        findViewById(R.id.btnSwap).setOnClickListener(v -> {
            int fromPos = fromSpinner.getSelectedItemPosition();
            fromSpinner.setSelection(toSpinner.getSelectedItemPosition());
            toSpinner.setSelection(fromPos);
            performConversion();
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performConversion();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performConversion();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentCategory + " Converter");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSpinners() {
        int arrayResId;
        switch (currentCategory) {
            case "Weight": arrayResId = R.array.weight_units; break;
            case "Temperature": arrayResId = R.array.temperature_units; break;
            case "Area": arrayResId = R.array.area_units; break;
            case "Volume": arrayResId = R.array.volume_units; break;
            case "Speed": arrayResId = R.array.speed_units; break;
            case "Pressure": arrayResId = R.array.pressure_units; break;
            case "Power": arrayResId = R.array.power_units; break;
            case "Currency": arrayResId = R.array.currency_units; break;
            case "NumberSystem": arrayResId = R.array.number_system_units; break;
            default: arrayResId = R.array.length_units; break;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    private void setupKeypad() {
        int[] buttonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btn00, R.id.btnDot
        };

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            String text = b.getText().toString();
            if (currentInput.toString().equals("0") && !text.equals(".")) {
                currentInput = new StringBuilder(text);
            } else {
                if (text.equals(".") && currentInput.toString().contains(".")) return;
                currentInput.append(text);
            }
            updateDisplay();
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btnAC).setOnClickListener(v -> {
            currentInput = new StringBuilder("0");
            updateDisplay();
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (currentInput.length() > 0) {
                currentInput.deleteCharAt(currentInput.length() - 1);
                if (currentInput.length() == 0) currentInput.append("0");
                updateDisplay();
            }
        });
    }

    private void updateDisplay() {
        fromValueText.setText(currentInput.toString());
        performConversion();
    }

    private void performConversion() {
        String inputStr = currentInput.toString();
        if (currentCategory.equals("NumberSystem")) {
            handleNumberSystemConversion(inputStr);
            return;
        }

        double value;
        try {
            value = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            toValueText.setText("0");
            return;
        }

        String fromUnit = fromSpinner.getSelectedItem().toString();
        String toUnit = toSpinner.getSelectedItem().toString();
        double result = calculateConversion(value, fromUnit, toUnit);
        toValueText.setText(String.format(Locale.getDefault(), "%.4f", result));
    }

    private void handleNumberSystemConversion(String input) {
        String from = fromSpinner.getSelectedItem().toString();
        String to = toSpinner.getSelectedItem().toString();
        try {
            int decimalValue;
            if (from.equals("Decimal")) decimalValue = Integer.parseInt(input);
            else if (from.equals("Binary")) decimalValue = Integer.parseInt(input, 2);
            else if (from.equals("Hexadecimal")) decimalValue = Integer.parseInt(input, 16);
            else if (from.equals("Octal")) decimalValue = Integer.parseInt(input, 8);
            else return;

            String result;
            if (to.equals("Decimal")) result = String.valueOf(decimalValue);
            else if (to.equals("Binary")) result = Integer.toBinaryString(decimalValue);
            else if (to.equals("Hexadecimal")) result = Integer.toHexString(decimalValue).toUpperCase();
            else if (to.equals("Octal")) result = Integer.toOctalString(decimalValue);
            else return;

            toValueText.setText(result);
        } catch (Exception e) {
            toValueText.setText("Error");
        }
    }

    private double calculateConversion(double value, String from, String to) {
        if (from.equals(to)) return value;

        double valueInBase = 0;
        
        switch (currentCategory) {
            case "Length":
                if (from.contains("(cm)")) valueInBase = value * 0.01;
                else if (from.contains("(m)")) valueInBase = value;
                else if (from.contains("(km)")) valueInBase = value * 1000;
                else if (from.contains("(in)")) valueInBase = value * 0.0254;
                else if (from.contains("(ft)")) valueInBase = value * 0.3048;
                else if (from.contains("(mi)")) valueInBase = value * 1609.34;

                if (to.contains("(cm)")) return valueInBase / 0.01;
                if (to.contains("(m)")) return valueInBase;
                if (to.contains("(km)")) return valueInBase / 1000;
                if (to.contains("(in)")) return valueInBase / 0.0254;
                if (to.contains("(ft)")) return valueInBase / 0.3048;
                if (to.contains("(mi)")) return valueInBase / 1609.34;
                break;

            case "Weight":
                if (from.contains("(g)")) valueInBase = value;
                else if (from.contains("(kg)")) valueInBase = value * 1000;
                else if (from.contains("(mg)")) valueInBase = value * 0.001;
                else if (from.contains("(lb)")) valueInBase = value * 453.592;
                else if (from.contains("(oz)")) valueInBase = value * 28.3495;

                if (to.contains("(g)")) return valueInBase;
                if (to.contains("(kg)")) return valueInBase / 1000;
                if (to.contains("(mg)")) return valueInBase / 0.001;
                if (to.contains("(lb)")) return valueInBase / 453.592;
                if (to.contains("(oz)")) return valueInBase / 28.3495;
                break;

            case "Temperature":
                double celsius;
                if (from.contains("(°C)")) celsius = value;
                else if (from.contains("(°F)")) celsius = (value - 32) * 5 / 9;
                else celsius = value - 273.15;

                if (to.contains("(°C)")) return celsius;
                if (to.contains("(°F)")) return (celsius * 9 / 5) + 32;
                return celsius + 273.15;

            case "Area":
                if (from.contains("(m²)")) valueInBase = value;
                else if (from.contains("(km²)")) valueInBase = value * 1000000;
                else if (from.contains("(cm²)")) valueInBase = value * 0.0001;
                else if (from.contains("(ft²)")) valueInBase = value * 0.092903;
                else if (from.equals("Acres")) valueInBase = value * 4046.86;
                else if (from.equals("Hectares")) valueInBase = value * 10000;

                if (to.contains("(m²)")) return valueInBase;
                if (to.contains("(km²)")) return valueInBase / 1000000;
                if (to.contains("(cm²)")) return valueInBase / 0.0001;
                if (to.contains("(ft²)")) return valueInBase / 0.092903;
                if (to.equals("Acres")) return valueInBase / 4046.86;
                if (to.equals("Hectares")) return valueInBase / 10000;
                break;

            case "Volume":
                if (from.contains("(L)")) valueInBase = value;
                else if (from.contains("(mL)")) valueInBase = value * 0.001;
                else if (from.contains("(m³)")) valueInBase = value * 1000;
                else if (from.contains("Gallons")) valueInBase = value * 3.78541;
                else if (from.contains("Cubic Feet")) valueInBase = value * 28.3168;

                if (to.contains("(L)")) return valueInBase;
                if (to.contains("(mL)")) return valueInBase / 0.001;
                if (to.contains("(m³)")) return valueInBase / 1000;
                if (to.contains("Gallons")) return valueInBase / 3.78541;
                if (to.contains("Cubic Feet")) return valueInBase / 28.3168;
                break;

            case "Speed":
                if (from.contains("(m/s)")) valueInBase = value;
                else if (from.contains("(km/h)")) valueInBase = value / 3.6;
                else if (from.contains("(mph)")) valueInBase = value * 0.44704;
                else if (from.equals("Knots")) valueInBase = value * 0.514444;

                if (to.contains("(m/s)")) return valueInBase;
                if (to.contains("(km/h)")) return valueInBase * 3.6;
                if (to.contains("(mph)")) return valueInBase / 0.44704;
                if (to.equals("Knots")) return valueInBase / 0.514444;
                break;

            case "Pressure":
                if (from.contains("(Pa)")) valueInBase = value;
                else if (from.equals("Bar")) valueInBase = value * 100000;
                else if (from.contains("(atm)")) valueInBase = value * 101325;
                else if (from.equals("PSI")) valueInBase = value * 6894.76;

                if (to.contains("(Pa)")) return valueInBase;
                if (to.equals("Bar")) return valueInBase / 100000;
                if (to.contains("(atm)")) return valueInBase / 101325;
                if (to.equals("PSI")) return valueInBase / 6894.76;
                break;

            case "Power":
                if (from.contains("(W)")) valueInBase = value;
                else if (from.contains("(kW)")) valueInBase = value * 1000;
                else if (from.contains("(hp)")) valueInBase = value * 745.7;

                if (to.contains("(W)")) return valueInBase;
                if (to.contains("(kW)")) return valueInBase / 1000;
                if (to.contains("(hp)")) return valueInBase / 745.7;
                break;

            case "Currency":
                 double rateToUsd;
                 if (from.contains("USD")) rateToUsd = 1.0;
                 else if (from.contains("EUR")) rateToUsd = 1.08;
                 else if (from.contains("INR")) rateToUsd = 0.012;
                 else if (from.contains("GBP")) rateToUsd = 1.27;
                 else rateToUsd = 0.0064;

                 double usdValue = value * rateToUsd;

                 if (to.contains("USD")) return usdValue;
                 if (to.contains("EUR")) return usdValue / 1.08;
                 if (to.contains("INR")) return usdValue / 0.012;
                 if (to.contains("GBP")) return usdValue / 1.27;
                 return usdValue / 0.0064;
        }
        return value;
    }
}