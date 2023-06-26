package com.codebykt.todo;

// LoginActivity.java

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        // Create or open the SQLite database
        database = openOrCreateDatabase("UserDB", MODE_PRIVATE, null);
        createTable();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void createTable() {
        // Create the "Users" table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS Users (username VARCHAR, password VARCHAR)");
    }

    private void login() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // Validate the entered credentials
        if (validateCredentials(username, password)) {
            // Successful login, navigate to the main activity or perform desired actions
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Invalid credentials, show error message
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Perform validation by querying the database for matching username and password
        Cursor cursor = database.rawQuery("SELECT * FROM Users WHERE username=? AND password=?", new String[]{username, password});
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    private void signUp() {
        // Handle sign-up logic, e.g., navigate to sign-up activity or fragment
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // Close the database connection
        database.close();
        super.onDestroy();
    }
}

