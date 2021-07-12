package com.example.haphapmessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailField, passField;
    AppCompatButton loginButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.login_activity_email_field);
        passField = findViewById(R.id.login_activity_password_field);
        loginButton = findViewById(R.id.login_activity_button_login);

        mAuth = FirebaseAuth.getInstance();

    }

    private Boolean validateEmail(){
        String val = Objects.requireNonNull(emailField.getEditText()).getText().toString();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            //email.setError("Field cannot be empty");
            return false;
       /* } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;*/
        } else {
            emailField.setError(null);
            emailField.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = Objects.requireNonNull(passField.getEditText()).getText().toString();

        if (val.isEmpty()) {
            //password.setError("Field cannot be empty");
            return false;
        } else {
            //password.setError(null);
            passField.setErrorEnabled(false);
            return true;
        }
    }

    //[START_sign_in_with_email_password]
    public void signInWithEmailAndPassword(View view) {

        if(validateEmail() && validatePassword()){
            mAuth.signInWithEmailAndPassword(Objects.requireNonNull(emailField.getEditText()).getText().toString(),
                    Objects.requireNonNull(passField.getEditText()).getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Authentication success and logged in", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authenitcation failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
    }
    //[END sign_in_with_email_pass]
}