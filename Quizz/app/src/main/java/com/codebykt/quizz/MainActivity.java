package com.codebykt.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAdminLogin;
    private Button btnParticipantLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        btnParticipantLogin = findViewById(R.id.btnParticipantLogin);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminLoginActivity
                Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        btnParticipantLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ParticipantLoginActivity
                Intent intent = new Intent(MainActivity.this, ParticipantActivity.class);
                startActivity(intent);
            }
        });
    }
}
