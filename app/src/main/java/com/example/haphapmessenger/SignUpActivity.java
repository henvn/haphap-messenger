package com.example.haphapmessenger;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = "SignupActivity";
    TextInputLayout nameField, emailField,passwordField,passConfirmField;
    AppCompatButton start_over;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference dRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameField = findViewById(R.id.sign_up_activity_name_field);
        emailField = findViewById(R.id.sign_up_activity_email_field);
        passwordField = findViewById(R.id.sign_up_activity_password_field);
        passConfirmField = findViewById(R.id.sign_up_activity_password_confirm_field);
        start_over = findViewById(R.id.sign_up_activity_start_over);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        db = FirebaseDatabase.getInstance();

        start_over.setOnClickListener(v->
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
    //VALIDATION METHODS
    private Boolean validateName(){
        String val = Objects.requireNonNull(nameField.getEditText()).getText().toString();
        if(val.isEmpty()){
            nameField.setError("Name required");
            return false;
        } else {
            nameField.setError(null);
            nameField.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = Objects.requireNonNull(emailField.getEditText()).getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            emailField.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(emailPattern)) {
            emailField.setError("Invalid email address");
            return false;
        } else {
            emailField.setError(null);
            emailField.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = Objects.requireNonNull(passwordField.getEditText()).getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if(val.isEmpty()){
            passwordField.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(passwordVal)) {
            passwordField.setError("The password must contain at least one lowercase character, one uppercase character," +
                    " one digit, one special character, and a length between 8 to 20.");
            return false;
        } else {
            passwordField.setError(null);
            passwordField.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean confirmPassword(){
        String val1 = Objects.requireNonNull(passwordField.getEditText()).getText().toString();
        String val2 = Objects.requireNonNull(passConfirmField.getEditText()).getText().toString();
        if(val1.isEmpty()){
            passConfirmField.setError("Field is empty");
            return false;
        } else if (!val1.equals(val2)){
            passConfirmField.setError("Passwords do not match");
            return false;
        } else {
            passConfirmField.setError(null);
            passConfirmField.setErrorEnabled(false);
            return true;
        }
    }


    //Register User
    public void registerNewUser(View view){
        //Field validation
        if(validateName() && validateEmail() && validatePassword() && confirmPassword()){
           //[START sign_up_user]
            mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(emailField.getEditText()).getText().toString(),
                    Objects.requireNonNull(passwordField.getEditText()).getText().toString())
                    .addOnCompleteListener(this, task ->
                    {
                        if(task.isSuccessful()){
                            pushToDatabase();
                            Log.d(TAG, "registerNewUser() sign up success");
                        } else {
                            Log.d(TAG, "registerNewUser() sign up failed");

                            String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                            errorMessages(errorCode);
                        }
                    });
            //[END sign_up_user]
        } else {
            Toast.makeText(this, "Invalid inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void errorMessages(String errorCode){
        if ("ERROR_EMAIL_ALREADY_IN_USE".equals(errorCode)) {
            Toast.makeText(SignUpActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
            emailField.setError("The email address is already in use by another account.");
            emailField.requestFocus();
        }
    }

    public void pushToDatabase(){
        String name = Objects.requireNonNull(nameField.getEditText()).getText().toString();
        String email = Objects.requireNonNull(emailField.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordField.getEditText()).getText().toString();

        Users user = new Users(name,email,password);

        dRef = db.getReference("Users");
        dRef.push().setValue(user).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(TAG,"Push to node success");
            } else {
                Log.d(TAG,"Failed to push to node");
            }
        });
    }

}