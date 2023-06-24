package com.codebykt.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ParticipantActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private Button buttonStartQuiz;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonStartQuiz = findViewById(R.id.buttonStartQuiz);

        firestore = FirebaseFirestore.getInstance();

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the participant name and email
                String participantName = editTextName.getText().toString().trim();
                String participantEmail = editTextEmail.getText().toString().trim();

                // Validate the participant name and email
                if (TextUtils.isEmpty(participantName) || TextUtils.isEmpty(participantEmail)) {
                    // Show an error message if the name or email is empty
                    editTextName.setError("Please enter your name");
                    editTextEmail.setError("Please enter your email");
                    return;
                }

                // Store the participant details
                storeParticipantDetails(participantName, participantEmail);

                // Navigate to QuizzActivity and pass the participant name and email as extra data
                Intent intent = new Intent(ParticipantActivity.this, QuizzActivity.class);
                intent.putExtra("participantName", participantName);
                intent.putExtra("participantEmail", participantEmail);
                startActivity(intent);
            }
        });
    }

    private void storeParticipantDetails(String name, String email) {
        // Create a new document in the "results" collection in Firestore
        // Add fields like "name" and "email" with null values if not provided

        DocumentReference resultRef = firestore.collection("results").document();
        Map<String, Object> participantData = new HashMap<>();
        participantData.put("name", name);
        participantData.put("email", email != null ? email : null);
        participantData.put("result", null);

        resultRef.set(participantData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Participant details stored successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to store participant details
                    }
                });
    }
}
