package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class EditNote extends AppCompatActivity {

    Intent data;
    private EditText editTitleOfNote,editContentOfNote;
    private FloatingActionButton saveEditNote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        data = getIntent();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        pd = new ProgressDialog(this);


        editTitleOfNote = findViewById(R.id.editTitleOfNote);
        editContentOfNote = findViewById(R.id.editContentOfNote);
        saveEditNote = findViewById(R.id.saveEditNote);
        Toolbar toolbar = findViewById(R.id.toolBarEditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        editTitleOfNote.setText(noteTitle);
        editContentOfNote.setText(noteContent);

        saveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTitle = editTitleOfNote.getText().toString();
                String newContent = editContentOfNote.getText().toString();
                if(newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(EditNote.this, "Both fields are required", Toast.LENGTH_SHORT).show();

                }else {
                    pd.setMessage("Updating.....");
                    pd.show();
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(EditNote.this,"Note Updated successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(EditNote.this,NotesActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(EditNote.this,"Failed to update note",Toast.LENGTH_SHORT).show();

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