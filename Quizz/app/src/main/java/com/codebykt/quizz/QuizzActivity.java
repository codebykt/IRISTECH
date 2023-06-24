package com.codebykt.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizzActivity extends AppCompatActivity {

    private TextView tvQuizzName;
    private LinearLayout questionContainer;
    private Button btnSubmitQuizz;
    private String[] participantAnswers;
    private List<Question> questions;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize variables
        tvQuizzName = findViewById(R.id.tvQuizzName);
        questionContainer = findViewById(R.id.questionContainer);
        btnSubmitQuizz = findViewById(R.id.btnSubmitQuizz);

        // Retrieve quiz ID from intent or use a constant value
        String quizId = "constquizid";

        // Get the reference to the quiz document in Firestore
        DocumentReference quizRef = db.collection("quizzes").document(quizId);

        // Retrieve the quiz data from Firestore
        quizRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Quiz data exists, retrieve and display the quiz name
                            String quizName = documentSnapshot.getString("quizName");
                            tvQuizzName.setText(quizName);

                            // Retrieve the list of questions
                            List<Map<String, Object>> questionsData = (List<Map<String, Object>>) documentSnapshot.get("questions");
                            questions = new ArrayList<>();
                            for (Map<String, Object> questionData : questionsData) {
                                String questionText = (String) questionData.get("questionText");
                                String correctAnswer = (String) questionData.get("correctAnswer");

                                List<String> options = new ArrayList<>();
                                options.add((String) questionData.get("option1"));
                                options.add((String) questionData.get("option2"));
                                options.add((String) questionData.get("option3"));
                                options.add((String) questionData.get("option4"));

                                Question question = new Question(questionText, options, correctAnswer);
                                questions.add(question);
                            }

                            // Now you can use the 'questions' list as needed
                            Log.d("FirestoreData", "Questions Size: " + questions.size());
                            initializeParticipantAnswers(questions.size()); // Initialize participant answers array
                            for (int i = 0; i < questions.size(); i++) {
                                Question question = questions.get(i);
                                addQuestionView(question, i);
                            }
                        } else {
                            // Quiz data does not exist
                            tvQuizzName.setText("Quiz Not Found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error retrieving quiz data
                        tvQuizzName.setText("Failed to retrieve quiz data");
                    }
                });

        // Set click listener for the submit button
        btnSubmitQuizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuizz();
            }
        });
    }

    private void initializeParticipantAnswers(int numQuestions) {
        participantAnswers = new String[numQuestions];
        for (int i = 0; i < numQuestions; i++) {
            participantAnswers[i] = "";
        }
    }

    private void addQuestionView(Question question, int questionIndex) {
        View questionView = getLayoutInflater().inflate(R.layout.question_item, questionContainer, false);
        TextView tvQuestion = questionView.findViewById(R.id.tv_question);
        RadioGroup radioGroupOptions = questionView.findViewById(R.id.radio_group);

        // Set the question text
        tvQuestion.setText(question.getQuestionText());

        // Create radio buttons for options
        for (int i = 0; i < question.getOptions().size(); i++) {
            String optionText = question.getOptions().get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(optionText);
            radioButton.setId(i);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Store participant's answer when an option is selected
                    participantAnswers[questionIndex] = optionText;
                }
            });
            radioGroupOptions.addView(radioButton);
        }

        // Add the question view to the container
        questionContainer.addView(questionView);
    }

    private void submitQuizz() {
        // Validate if all questions have been answered
        for (String answer : participantAnswers) {
            if (answer.isEmpty()) {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Calculate score
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String participantAnswer = participantAnswers[i];
            if (participantAnswer.equals(question.getCorrectAnswer())) {
                score++;
            }
        }

        // Retrieve participant name and email from intent
        Intent intent = getIntent();
        String participantName = intent.getStringExtra("participantName");
        String participantEmail = intent.getStringExtra("participantEmail");

        // Show score or perform any other action
        Toast.makeText(this, "Your score: " + score, Toast.LENGTH_SHORT).show();

        // Upload result to Firestore
        uploadResult(participantName, participantEmail, score);

        // Start a new activity or perform any other action
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("score", score);
        startActivity(i);
    }

    private void uploadResult(String name, String email, int score) {
        // Get the authenticated user
        FirebaseUser user = auth.getCurrentUser();

        // Ensure that the user is authenticated
        if (user != null) {
            String participantId = user.getUid();

            // Construct the document path using the participantId
            String resultPath = "results/" + participantId;

            // Create a new result document with participant data
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("name", name);
            resultData.put("email", email);
            resultData.put("score", score);

            // Write the participant result to Firestore
            db.document(resultPath)
                    .set(resultData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("UploadResult", "Result uploaded successfully!");
                            // Handle success, such as displaying a success message or navigating to another activity
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("UploadResult", "Failed to upload result", e);
                            // Handle failure, such as displaying an error message
                        }
                    });
        }
    }
}
