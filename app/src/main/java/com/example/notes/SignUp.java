package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText signUpEmail,signUpPassword;
    private Button signup;
    private TextView gotoLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signup = findViewById(R.id.signup);
        pd = new ProgressDialog(this);
        gotoLogin = findViewById(R.id.gotoLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUp.this, "All fields are required", Toast.LENGTH_LONG).show();
                }else if(password.length() <= 7){
                    Toast.makeText(SignUp.this, "Password  should have greater than 7 digits ", Toast.LENGTH_LONG).show();
                }else {
                    pd.setMessage("Creating account.....");
                    pd.show();
firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            pd.dismiss();
            Toast.makeText(SignUp.this,"Registration is successful",Toast.LENGTH_SHORT).show();
            sendEmailVerification();

        }else {
            pd.dismiss();
            Toast.makeText(SignUp.this,"Failed to register",Toast.LENGTH_LONG).show();

        }

    }
});


                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUp.this,"Verification email is sent, Verify and Log In again",Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
            });
        }else {
            Toast.makeText(SignUp.this,"Failed to send verification email",Toast.LENGTH_LONG).show();
        }
    }
}