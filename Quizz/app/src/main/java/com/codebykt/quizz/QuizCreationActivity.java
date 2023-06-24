package com.codebykt.quizz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizCreationActivity extends AppCompatActivity {

    private EditText etQuizName;
    private LinearLayout layoutQuestions;
    private Button btnAddQuestion;
    private Button btnPublishQuiz;

    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);

        etQuizName = findViewById(R.id.etQuizName);
        layoutQuestions = findViewById(R.id.questionContainer);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnPublishQuiz = findViewById(R.id.btnPublishQuiz);

        questionList = new ArrayList<>();

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionForm();
            }
        });

        btnPublishQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishQuiz();
            }
        });
    }

    private void addQuestionForm() {
        View questionView = getLayoutInflater().inflate(R.layout.question_form, null);
        layoutQuestions.addView(questionView);
    }

    private void publishQuiz() {
        String quizName = etQuizName.getText().toString().trim();

        // Validate quiz name and questions
        if (validateQuizData(quizName)) {
            // Upload quiz data to Firestore
            uploadQuizDataToFirestore(quizName);
        }
    }



    private boolean validateQuizData(String quizName) {
        if (quizName.isEmpty()) {
            etQuizName.setError("Please enter a quiz name");
            return false;
        }

        int questionCount = layoutQuestions.getChildCount();
        if (questionCount == 0) {
            // No questions added
            // Show error message or handle accordingly
            return false;
        }

        // Validate each question form
        for (int i = 0; i < questionCount; i++) {
            View questionView = layoutQuestions.getChildAt(i);

            EditText etQuestion = questionView.findViewById(R.id.questionEditText);
            EditText etOption1 = questionView.findViewById(R.id.option1EditText);
            EditText etOption2 = questionView.findViewById(R.id.option2EditText);
            EditText etOption3 = questionView.findViewById(R.id.option3EditText);
            EditText etOption4 = questionView.findViewById(R.id.option4EditText);
            Spinner spinnerCorrectAnswer = questionView.findViewById(R.id.correctAnswerSpinner);

            String questionText = etQuestion.getText().toString().trim();
            String option1 = etOption1.getText().toString().trim();
            String option2 = etOption2.getText().toString().trim();
            String option3 = etOption3.getText().toString().trim();
            String option4 = etOption4.getText().toString().trim();
            String correctAnswer = spinnerCorrectAnswer.getSelectedItem().toString();

            // Validate each field of the question form
            // You can add your own validation logic here

            if (questionText.isEmpty()) {
                etQuestion.setError("Please enter a question");
                return false;
            }

            if (option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
                // Show error message or handle accordingly
                Toast.makeText(QuizCreationActivity.this,"All 4  options are requied",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void uploadQuizDataToFirestore(String quizName) {
        String quizId = "constquizid";
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Create a new quiz document in the "quizzes" collection
        DocumentReference quizRef = firestore.collection("quizzes").document(quizId);

        // Create a map to store the quiz data
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("quizName", quizName);

        // Retrieve the questions from the layout
        int questionCount = layoutQuestions.getChildCount();
        List<Map<String, Object>> questionList = new ArrayList<>();

        for (int i = 0; i < questionCount; i++) {
            View questionView = layoutQuestions.getChildAt(i);

            EditText etQuestion = questionView.findViewById(R.id.questionEditText);
            EditText etOption1 = questionView.findViewById(R.id.option1EditText);
            EditText etOption2 = questionView.findViewById(R.id.option2EditText);
            EditText etOption3 = questionView.findViewById(R.id.option3EditText);
            EditText etOption4 = questionView.findViewById(R.id.option4EditText);
            Spinner spinnerCorrectAnswer = questionView.findViewById(R.id.correctAnswerSpinner);

            String questionText = etQuestion.getText().toString().trim();
            String option1 = etOption1.getText().toString().trim();
            String option2 = etOption2.getText().toString().trim();
            String option3 = etOption3.getText().toString().trim();
            String option4 = etOption4.getText().toString().trim();
            String correctAnswer = "";

            int selectedOptionPosition = spinnerCorrectAnswer.getSelectedItemPosition();

            switch (selectedOptionPosition) {
                case 0:
                    correctAnswer = option1;
                    break;
                case 1:
                    correctAnswer = option2;
                    break;
                case 2:
                    correctAnswer = option3;
                    break;
                case 3:
                    correctAnswer = option4;
                    break;
                default:
                    correctAnswer = ""; // Set a default value or handle the case when no option is selected
            }

            // Create a map to store the question data
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("questionText", questionText);
            questionData.put("option1", option1);
            questionData.put("option2", option2);
            questionData.put("option3", option3);
            questionData.put("option4", option4);
            questionData.put("correctAnswer", correctAnswer);

            // Add the question data to the list
            questionList.add(questionData);
        }

        // Add the question list to the quiz data
        quizData.put("questions", questionList);

        // Upload the quiz data to Firestore
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.set(quizRef, quizData);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Quiz data uploaded successfully
                Toast.makeText(QuizCreationActivity.this, "Quiz published successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error uploading quiz data
                Toast.makeText(QuizCreationActivity.this, "Failed to publish quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
