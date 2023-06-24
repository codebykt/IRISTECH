package com.codebykt.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvScore;
    private Button btnFinish;
    private Button btnGoToGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        tvResult = findViewById(R.id.tvResult);
        tvScore = findViewById(R.id.tvScore);
        btnFinish = findViewById(R.id.btnFinish);
        btnGoToGitHub = findViewById(R.id.btnGoToGitHub);

        // Retrieve the score from the intent
        int score = getIntent().getIntExtra("score", 0);

        // Display the score
        tvScore.setText("Your Score: " + score);

        // Set click listener for the finish button
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and return to the main activity
                Intent i = new Intent(ResultActivity.this,ParticipantActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Set click listener for the Go to GitHub button
        btnGoToGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a web browser and navigate to the GitHub page
                String url = "https://github.com/codebykt";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}
