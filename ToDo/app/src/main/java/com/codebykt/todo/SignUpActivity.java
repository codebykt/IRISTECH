package com.codebykt.todo;

// SignUpActivity.java

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSignUp;
    private TextView textViewLogin;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Create or open the SQLite database
        database = openOrCreateDatabase("UserDB", MODE_PRIVATE, null);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void signUp() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // Validate the entered information
        if (validateInput(username, password, confirmPassword)) {
            // Save the user's credentials to the database
            saveCredentials(username, password);

            Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show();

            // Navigate to the login activity or perform desired actions
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Invalid input, show error message
            Toast.makeText(this, "Invalid input or passwords don't match", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String username, String password, String confirmPassword) {
        // Perform validation for input fields, e.g., check for empty fields, password match, etc.
        return !username.isEmpty() && !password.isEmpty() && password.equals(confirmPassword);
    }

    private void saveCredentials(String username, String password) {
        // Save the user's credentials to the database
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        database.insert("Users", null, values);
    }

    private void login() {
        // Handle login navigation or logic if the user already has an account
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        // Close the database connection
        database.close();
        super.onDestroy();
    }
}
