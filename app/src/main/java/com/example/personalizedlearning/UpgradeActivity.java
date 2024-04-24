package com.example.personalizedlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class UpgradeActivity extends AppCompatActivity {
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991; // Unique identifier for Google Pay requests
    private PaymentsClient paymentsClient; // Client for Google Pay API calls

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        paymentsClient = createGooglePayClient(); // Initialize Google Pay client
        initGooglePay();

        findViewById(R.id.btn_starter_purchase).setOnClickListener(v -> createGooglePayRequest("10.00")); // Example price for Starter
        findViewById(R.id.btn_intermediate_purchase).setOnClickListener(v -> createGooglePayRequest("20.00")); // Example price for Intermediate
        findViewById(R.id.btn_advanced_purchase).setOnClickListener(v -> createGooglePayRequest("30.00")); // Example price for Advanced

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish()); // Close the activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                PaymentData paymentData = PaymentData.getFromIntent(data);
                handlePaymentSuccess(paymentData);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle the user canceling the payment interaction
                Toast.makeText(this, "Payment canceled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        String paymentInfo = paymentData.toJson();
        // Process payment information here
        Toast.makeText(this, "Payment was successful", Toast.LENGTH_LONG).show();
    }

    private void initGooglePay() {
        // Check if Google Pay is available and ready
        possiblyShowGooglePayButton();
    }

    private PaymentsClient createGooglePayClient() {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // Use ENVIRONMENT_PRODUCTION for live payments
                .build();
        return Wallet.getPaymentsClient(this, walletOptions);
    }

    private void possiblyShowGooglePayButton() {
        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethods(Arrays.asList(
                        WalletConstants.PAYMENT_METHOD_CARD,
                        WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
                ))
                .build();
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                // Show Google Pay as an option in your app
                updateUI(completedTask.getResult());
            } else {
                Log.w("Google Pay API", "isReadyToPay failed", completedTask.getException());
            }
        });
    }

    private void updateUI(boolean isReadyToPay) {
        if (isReadyToPay) {
            // Show Google Pay buttons
            findViewById(R.id.btn_starter_purchase).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_intermediate_purchase).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_advanced_purchase).setVisibility(View.VISIBLE);
        } else {
            // Hide Google Pay buttons or inform the user that Google Pay is not available
            Toast.makeText(this, "Google Pay not available", Toast.LENGTH_LONG).show();
        }
    }

    private void createGooglePayRequest(String price) {
        try {
            JSONObject paymentDataRequestJson = new JSONObject();
            paymentDataRequestJson.put("apiVersion", 2);
            paymentDataRequestJson.put("apiVersionMinor", 0);
            JSONObject allowedPaymentMethods = new JSONObject();
            allowedPaymentMethods.put("type", "CARD");

            JSONObject parameters = new JSONObject();
            parameters.put("allowedAuthMethods", new JSONArray(Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS")));
            parameters.put("allowedCardNetworks", new JSONArray(Arrays.asList("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA")));

            allowedPaymentMethods.put("parameters", parameters);

            paymentDataRequestJson.put("allowedPaymentMethods", new JSONArray().put(allowedPaymentMethods));
            JSONObject transactionInfo = new JSONObject();
            transactionInfo.put("totalPrice", price);
            transactionInfo.put("totalPriceStatus", "FINAL");
            transactionInfo.put("currencyCode", "USD");

            paymentDataRequestJson.put("transactionInfo", transactionInfo);

            paymentDataRequestJson.put("merchantInfo", new JSONObject()
                    .put("merchantName", "Example Merchant"));

            // This is a tokenizationSpecification for test environment
            JSONObject tokenizationSpecification = new JSONObject();
            tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
            tokenizationSpecification.put("parameters", new JSONObject()
                    .put("gateway", "example")
                    .put("gatewayMerchantId", "exampleGatewayMerchantId"));

            allowedPaymentMethods.put("tokenizationSpecification", tokenizationSpecification);

            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this,
                        LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
