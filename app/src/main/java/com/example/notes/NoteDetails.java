package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetails extends AppCompatActivity {


    private TextView titleOfNoteDetails;
    private TextView contentOfNoteDetails;
    private FloatingActionButton gotoEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        contentOfNoteDetails = findViewById(R.id.ContentOfNoteDetails);
        titleOfNoteDetails = findViewById(R.id.TitleOfNoteDetails);
        gotoEditNote = findViewById(R.id.gotoEditNote);
        Toolbar toolbar = findViewById(R.id.toolBarNoteDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        gotoEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteDetails.this,EditNote.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                startActivity(intent);

            }
        });

        titleOfNoteDetails.setText(data.getStringExtra("title"));
        contentOfNoteDetails.setText(data.getStringExtra("content"));


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}