package com.codebykt.quizz;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QuizCreationActivity extends AppCompatActivity {
    private EditText quizNameEditText;
    private LinearLayout questionsLayout;
    private Button addQuestionButton;
    private Button publishButton;

    private FirebaseFirestore firestore;

    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);

        firestore = FirebaseFirestore.getInstance();

        quizNameEditText = findViewById(R.id.quizNameEditText);
        questionsLayout = findViewById(R.id.questionsLayout);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        publishButton = findViewById(R.id.publishButton);

        questionList = new ArrayList<>();

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionView();
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quizName = quizNameEditText.getText().toString().trim();

                if (quizName.isEmpty() || questionList.isEmpty()) {
                    Toast.makeText(QuizCreationActivity.this, "Please fill in the quiz name and add questions", Toast.LENGTH_SHORT).show();
                } else {
                    Quiz quiz = new Quiz(quizName, questionList);
                    uploadQuiz(quiz);
                }
            }
        });

        // Add the initial question view
        addQuestionView();
    }

    private void addQuestionView() {
        View questionView = getLayoutInflater().inflate(R.layout.question_layout, null);
        questionsLayout.addView(questionView);

        EditText questionEditText = questionView.findViewById(R.id.questionEditText);
        EditText option1EditText = questionView.findViewById(R.id.option1EditText);
        EditText option2EditText = questionView.findViewById(R.id.option2EditText);
        EditText option3EditText = questionView.findViewById(R.id.option3EditText);
        EditText option4EditText = questionView.findViewById(R.id.option4EditText);
        Spinner correctAnswerSpinner = questionView.findViewById(R.id.correctAnswerSpinner);

        String questionText = questionEditText.getText().toString();
        String option1Text = option1EditText.getText().toString();
        String option2Text = option2EditText.getText().toString();
        String option3Text = option3EditText.getText().toString();
        String option4Text = option4EditText.getText().toString();
        int correctAnswerIndex = correctAnswerSpinner.getSelectedItemPosition();



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Option 1", "Option 2", "Option 3", "Option 4"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correctAnswerSpinner.setAdapter(adapter);

        Question question = new Question(questionText, option1Text, option2Text, option3Text, option4Text, correctAnswerIndex);

        questionList.add(question);
    }

    private void uploadQuiz(Quiz quiz) {
        // Upload the quiz to Firebase Firestore
        firestore.collection("quizzes")
                .add(quiz)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(QuizCreationActivity.this, "Quiz published successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(QuizCreationActivity.this, "Failed to publish quiz", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        quizNameEditText.setText("");
        questionsLayout.removeAllViews();
        questionList.clear();
        addQuestionView();
    }
}
