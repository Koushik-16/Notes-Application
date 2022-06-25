package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateNotes extends AppCompatActivity {

    EditText createTitleOfNote,createContentOfNote;

FloatingActionButton saveNote;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
FirebaseFirestore firebaseFirestore;

private ProgressDialog pd;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        createContentOfNote = findViewById(R.id.createContentOfNote);
        createTitleOfNote = findViewById(R.id.createTitleOfNote);
        saveNote = findViewById(R.id.saveNote);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        pd = new ProgressDialog(this);


        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = createTitleOfNote.getText().toString();
                String content = createContentOfNote.getText().toString();
                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(CreateNotes.this, "Both fields are required", Toast.LENGTH_SHORT).show();

                }else {
                    pd.setMessage("Saving....");
                    pd.show();
                    DocumentReference  documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(CreateNotes.this,"Note created successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(CreateNotes.this,NotesActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(CreateNotes.this,"Failed to create note",Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}