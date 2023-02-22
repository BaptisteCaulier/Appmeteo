package com.example.projetmeteo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class NotesEditActivity extends AppCompatActivity
{
    //declaring the variables
    private EditText editText_title;
    private EditText editText_text;
    private Button button_save;
    private Button button_delete;
    private Button button_back;
    private String note;
    private String id;
    private String city;
    private String title;
    private String text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_edit);

        //affecting the ids to the variables and set the background color
        this.editText_title = findViewById(R.id.title);
        this.editText_text = findViewById(R.id.text);
        this.button_save = findViewById(R.id.save);
        button_save.setBackgroundColor(0XFF0072D7);
        this.button_delete = findViewById(R.id.delete);
        button_delete.setBackgroundColor(0XFF0072D7);
        this.button_back = findViewById(R.id.back);
        button_back.setBackgroundColor(0XFF0072D7);

        Intent intent = getIntent();
        this.note = intent.getStringExtra("note");
        //here if note is null, it means that the user clicked on new note, so we only get the selected city
        if (note==null)
        {
            this.city = intent.getStringExtra("city");
        }
        //if it's not null, it means that the user clicked on an existing note, so we affect the different parts of the note to the variables
        else
        {
            String id_temp = note.substring(note.indexOf("id="),note.indexOf(", city"));
            id = id_temp.replace("id=", "");

            String city_temp = note.substring(note.indexOf("city='"),note.indexOf("', title"));
            city = city_temp.replace("city='", "");

            String title_temp = note.substring(note.indexOf("title='"),note.indexOf("', text"));
            title = title_temp.replace("title='", "");

            String text_temp = note.substring(note.indexOf("text='"),note.indexOf("'}"));
            text = text_temp.replace("text='", "");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //here we display the title and text if it's an existing note
        if (note!=null)
        {
            editText_title.setText(title);
            editText_text.setText(text);
        }

        //the back button sends the user to pages with the notes
        button_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(NotesEditActivity.this, NotesActivity.class);
                //here we get the item clicked in parameters
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        //the delete button deletes the notes for the city in the database
        button_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //if the note is already created we delete it
                if (note!=null)
                {
                    Executors.newSingleThreadExecutor().execute(() ->
                    {
                        //we call the delete function
                        NotesDao notesDao = MyDatabase.getDatabase(NotesEditActivity.this).notesDao();
                        notesDao.delete(Integer.parseInt(id));
                    });
                    //we display a message for the user
                    Toast.makeText(NotesEditActivity.this, "Notes deleted successfully", Toast.LENGTH_SHORT).show();
                    //we start back to the notes list activity
                    Intent intent = new Intent(NotesEditActivity.this, NotesActivity.class);
                    //here we get the item clicked in parameters
                    intent.putExtra("city", city);
                    startActivity(intent);
                }
                //if the note is new we display a message
                else
                {
                    Toast.makeText(NotesEditActivity.this, "Can't delete a new note", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //the save button saves the notes for the city in the database
        button_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //here we check if the title and text are not empty
                if (String.valueOf(editText_title.getText()).equals("") || String.valueOf(editText_text.getText()).equals(""))
                {
                    Toast.makeText(NotesEditActivity.this, "Title and content are needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                Executors.newSingleThreadExecutor().execute(() ->
                {
                    NotesDao notesDao = MyDatabase.getDatabase(NotesEditActivity.this).notesDao();
                    //here if it's an existing note, we update it in the database
                    if (note!=null)
                    {
                        notesDao.update(Integer.parseInt(id),String.valueOf(editText_title.getText()), String.valueOf(editText_text.getText()));
                    }
                    //if it's a new note, we insert it in the database
                    else
                    {
                        notesDao.insert(new Notes(null, city, String.valueOf(editText_title.getText()), String.valueOf(editText_text.getText())));
                    }

                });
                Toast.makeText(NotesEditActivity.this, "Note saved successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
