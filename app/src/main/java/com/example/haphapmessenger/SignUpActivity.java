package com.example.haphapmessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    AppCompatButton start_over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        start_over = findViewById(R.id.sign_up_activity_start_over);

        start_over.setOnClickListener(v->
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

    }
}