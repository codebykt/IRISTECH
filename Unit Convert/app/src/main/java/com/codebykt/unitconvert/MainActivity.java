package com.codebykt.unitconvert;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private Spinner spinnerFromUnit;
    private Spinner spinnerToUnit;
    private Button buttonConvert;
    private TextView textViewConvertedValue;
    private Button buttonGitHub;

    private String[] units = {"Centimeters", "Meters", "Millimeters", "Grams", "Kilograms", "Milligrams", "Minutes", "Hours", "Seconds", "Milliseconds"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = findViewById(R.id.editTextInput);
        spinnerFromUnit = findViewById(R.id.spinnerFromUnit);
        spinnerToUnit = findViewById(R.id.spinnerToUnit);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewConvertedValue = findViewById(R.id.textViewConvertedValue);
        buttonGitHub = findViewById(R.id.buttonGitHub);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerFromUnit.setAdapter(unitAdapter);
        spinnerToUnit.setAdapter(unitAdapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });

        buttonGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGitHubPage();
            }
        });
    }

    private void convertUnits() {
        String inputString = editTextInput.getText().toString().trim();
        if (inputString.isEmpty()) {
            textViewConvertedValue.setText("");
            return;
        }

        double inputValue = Double.parseDouble(inputString);
        int fromUnitIndex = spinnerFromUnit.getSelectedItemPosition();
        int toUnitIndex = spinnerToUnit.getSelectedItemPosition();

        double convertedValue;

        // Convert units based on fromUnitIndex and toUnitIndex
        if (fromUnitIndex == 0 && toUnitIndex == 1) {
            convertedValue = inputValue * 0.01; // Centimeters to Meters
        } else if (fromUnitIndex == 0 && toUnitIndex == 2) {
            convertedValue = inputValue * 10; // Centimeters to Millimeters
        } else if (fromUnitIndex == 1 && toUnitIndex == 0) {
            convertedValue = inputValue * 100; // Meters to Centimeters
        } else if (fromUnitIndex == 1 && toUnitIndex == 2) {
            convertedValue = inputValue * 1000; // Meters to Millimeters
        } else if (fromUnitIndex == 2 && toUnitIndex == 0) {
            convertedValue = inputValue * 0.1; // Millimeters to Centimeters
        } else if (fromUnitIndex == 2 && toUnitIndex == 1) {
            convertedValue = inputValue * 0.001; // Millimeters to Meters
        } else if (fromUnitIndex == 3 && toUnitIndex == 4) {
            convertedValue = inputValue * 0.001; // Grams to Kilograms
        } else if (fromUnitIndex == 3 && toUnitIndex == 5) {
            convertedValue = inputValue * 1000; // Grams to Milligrams
        } else if (fromUnitIndex == 4 && toUnitIndex == 3) {
            convertedValue = inputValue * 1000; // Kilograms to Grams
        } else if (fromUnitIndex == 4 && toUnitIndex == 5) {
            convertedValue = inputValue * 1000000; // Kilograms to Milligrams
        } else if (fromUnitIndex == 5 && toUnitIndex == 3) {
            convertedValue = inputValue * 0.001; // Milligrams to Grams
        } else if (fromUnitIndex == 5 && toUnitIndex == 4) {
            convertedValue = inputValue * 0.000001; // Milligrams to Kilograms
        } else if (fromUnitIndex == 6 && toUnitIndex == 7) {
            convertedValue = inputValue / 60; // Minutes to Hours
        } else if (fromUnitIndex == 6 && toUnitIndex == 8) {
            convertedValue = inputValue * 60; // Minutes to Seconds
        } else if (fromUnitIndex == 6 && toUnitIndex == 9) {
            convertedValue = inputValue * 60000; // Minutes to Milliseconds
        } else if (fromUnitIndex == 7 && toUnitIndex == 6) {
            convertedValue = inputValue * 60; // Hours to Minutes
        } else if (fromUnitIndex == 7 && toUnitIndex == 8) {
            convertedValue = inputValue * 3600; // Hours to Seconds
        } else if (fromUnitIndex == 7 && toUnitIndex == 9) {
            convertedValue = inputValue * 3600000; // Hours to Milliseconds
        } else if (fromUnitIndex == 8 && toUnitIndex == 6) {
            convertedValue = inputValue / 60; // Seconds to Minutes
        } else if (fromUnitIndex == 8 && toUnitIndex == 7) {
            convertedValue = inputValue / 3600; // Seconds to Hours
        } else if (fromUnitIndex == 8 && toUnitIndex == 9) {
            convertedValue = inputValue * 1000; // Seconds to Milliseconds
        } else if (fromUnitIndex == 9 && toUnitIndex == 6) {
            convertedValue = inputValue / 60000; // Milliseconds to Minutes
        } else if (fromUnitIndex == 9 && toUnitIndex == 7) {
            convertedValue = inputValue / 3600000; // Milliseconds to Hours
        } else if (fromUnitIndex == 9 && toUnitIndex == 8) {
            convertedValue = inputValue * 0.001; // Milliseconds to Seconds
        } else {
            convertedValue = inputValue; // No conversion needed
            textViewConvertedValue.setText("Invalid Conversion");
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        String formattedValue = decimalFormat.format(convertedValue);

        textViewConvertedValue.setText(formattedValue);
    }

    public void openGitHubPage() {
        String url = "https://github.com/codebykt";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
