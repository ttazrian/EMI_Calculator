package com.example.emi_calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    //declaring UI components
    EditText loanAmountInput, interestRateInput, timePeriodInput;
    ChipGroup chipSelection, paymentFrequencyGroup;
    Chip chipMonths, chipYears, chipMonthly, chipBiWeekly, chipWeekly;
    Button calculateButton;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Links each UI element to variables to get info and input
        loanAmountInput = findViewById(R.id.loanAmount);
        interestRateInput = findViewById(R.id.interestRate);
        timePeriodInput = findViewById(R.id.timePeriod);
        chipSelection = findViewById(R.id.chipSelection);
        paymentFrequencyGroup = findViewById(R.id.paymentFrequencyGroup);
        chipMonths = findViewById(R.id.chipMonths);
        chipYears = findViewById(R.id.chipYears);
        chipMonthly = findViewById(R.id.chipMonthly);
        chipBiWeekly = findViewById(R.id.chipBiWeekly);
        chipWeekly = findViewById(R.id.chipWeekly);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.result);

        // Calculate button -- setting on click listener
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateEMI();
            }
        });
    }

    // Calculate the EMI
    private void calculateEMI() {
        // Get user input and convert to data types as required
        String loanAmountStr = loanAmountInput.getText().toString();
        String interestRateStr = interestRateInput.getText().toString();
        String timePeriodStr = timePeriodInput.getText().toString();

        // Validate input-- ensure all values are there
        if (loanAmountStr.isEmpty() || interestRateStr.isEmpty() || timePeriodStr.isEmpty()) {
            resultTextView.setText("Please fill in all fields.");
            return;
        }

        // Convert inputs to numerical values
        double loanAmount = Double.parseDouble(loanAmountStr); // Principal Loan Amount
        double annualInterestRate = Double.parseDouble(interestRateStr); // Annual Interest Rate in percentage
        double timePeriod = Double.parseDouble(timePeriodStr); // Length of loan period in either months or years

        // Check which unit is selected (Months or Years)
        if (chipYears.isChecked()) {
            timePeriod = timePeriod * 12; // Convert years to months if "Years" is selected
        }

        // Default payment frequency is set to monthly
        int paymentsPerYear = 12;

        // Check which payment frequency is selected (Monthly, Bi-weekly, Weekly)
        if (chipBiWeekly.isChecked()) {
            paymentsPerYear = 26; // Bi-weekly (26 payments per year)
        } else if (chipWeekly.isChecked()) {
            paymentsPerYear = 52; // Weekly (52 payments per year)
        }

        // Calculate the interest rate per payment period based on the selected payment frequency
        double interestRatePerPayment = (annualInterestRate / paymentsPerYear) / 100;
        double totalPayments = timePeriod * (paymentsPerYear / 12); // Adjust total payments based on the frequency

        // EMI Calculation using formula (accounts for selected payment freuency)
        double emi = (loanAmount * interestRatePerPayment * Math.pow(1 + interestRatePerPayment, totalPayments))
                / (Math.pow(1 + interestRatePerPayment, totalPayments) - 1);

        // Print adn display the result
        resultTextView.setText(String.format("Your EMI is: $%.2f (%s)", emi, getPaymentFrequencyText()));
    }

    // Helper method to get payment frequency text
    private String getPaymentFrequencyText() {
        if (chipBiWeekly.isChecked()) {
            return "Bi-weekly";
        } else if (chipWeekly.isChecked()) {
            return "Weekly";
        }
        return "Monthly"; // Default to monthly
    }
}
