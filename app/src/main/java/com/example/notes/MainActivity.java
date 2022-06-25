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

public class MainActivity extends AppCompatActivity {
    private Button gotoSignUp;
    private EditText loginEmail,loginPassword;
    private Button login;
    private TextView gotoForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoSignUp = findViewById(R.id.gotoSignup);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        login = findViewById(R.id.login);
        pd = new ProgressDialog(this);
        gotoForgotPassword = findViewById(R.id.gotoForgotPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }
        gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });
        gotoForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All Fields Are Required", Toast.LENGTH_LONG).show();
                }else {
                    pd.setMessage("Logging in ....");
                    pd.show();

                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) checkMailVerification();
                            else {
                                pd.dismiss();
                                Toast.makeText(MainActivity.this,"Account does not exist",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

    }

    private void checkMailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()){
            Toast.makeText(MainActivity.this,"Loged In",Toast.LENGTH_SHORT).show();
            finish();
            pd.dismiss();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }else {
            pd.dismiss();
            Toast.makeText(MainActivity.this,"Verify your email first",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}