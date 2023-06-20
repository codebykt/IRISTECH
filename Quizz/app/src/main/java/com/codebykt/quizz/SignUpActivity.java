package com.codebykt.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform signup operation here using Firebase Authentication or your preferred authentication method
                    // You can use the email and password values for signing up the admin
                    // After successful signup, navigate to the MainActivity

                    signupButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = emailEditText.getText().toString().trim();
                            String password = passwordEditText.getText().toString().trim();
                            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                                Toast.makeText(SignupActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                            } else if (!password.equals(confirmPassword)) {
                                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                            } else {
                                // Perform signup operation using Firebase Authentication
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Signup success
                                                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                                    navigateToMainActivity();
                                                } else {
                                                    // Signup failed
                                                    Toast.makeText(SignupActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });

                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                }
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Call finish() to remove the SignupActivity from the back stack
    }
}
