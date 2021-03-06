package com.example.afs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class Signup extends firebaseActivity {


    private EditText emailInput;
    private EditText confirmInput;
    private EditText passwordInput;
    private Button signUpButton;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        signUpButton = (Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signUp();
            }
        });


        //TODO check with database to
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart()
    {
        super.onStart();

        if(mAuth.getCurrentUser() != null)
        {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signUp()
    {
        emailInput = (EditText)findViewById(R.id.emailInput);
        confirmInput = (EditText)findViewById(R.id.confirmInput);
        passwordInput = (EditText)findViewById(R.id.passwordInput);

        if (TextUtils.isEmpty(emailInput.getText().toString())) {
            emailInput.setError("Required");
            return;
        } else {
            emailInput.setError(null);
        }
        int index = emailInput.getText().toString().indexOf("@");

        if (index == -1 || index == 0 || index == emailInput.getText().toString().length()-1) {
            emailInput.setError("Invalid");
            return;
        } else {
            emailInput.setError(null);
        }



        if (TextUtils.isEmpty(passwordInput.getText().toString())) {
            passwordInput.setError("Required");
            return;
        } else {
            passwordInput.setError(null);
        }

        if (TextUtils.isEmpty(confirmInput.getText().toString())) {
            confirmInput.setError("Required");
            return;
        } else {
            confirmInput.setError(null);
        }

        if (!confirmInput.getText().toString().equals(passwordInput.getText().toString())) {
            confirmInput.setError("Mismatch");
            return;
        } else {
            confirmInput.setError(null);
        }

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirm = confirmInput.getText().toString();



        //TODO check with database

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Signup", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onAuthSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Signup", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }


    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());


        // Go to MainActivity
        startActivity(new Intent(Signup.this, MainActivity.class));
        finish();
    }

    private void writeNewUser(String userId, String name, String email) {
        UserInfo user = new UserInfo();
        user.setUserName(name);
        user.setEmail(email);
        user.setGender(Gender.UNKNOWN);
        user.setAge(0);
        Map<String, Object> userValue = user.toMap();

        db.child("Users").child(userId).updateChildren(userValue);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
