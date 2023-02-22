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
    //declaration des variables
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

        //affectation des id aux variables et couleurs des boutons
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
        //on regarde si il y a un note
        if (note==null)
        {
            this.city = intent.getStringExtra("city");
        }
        //recuperation des informations de la note
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

        //affichage
        if (note!=null)
        {
            editText_title.setText(title);
            editText_text.setText(text);
        }

        //bouton retour en arriere
        button_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(NotesEditActivity.this, NotesActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        //suppresion d'une note
        button_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (note!=null)
                {
                    Executors.newSingleThreadExecutor().execute(() ->
                    {
                        //appel de la requete
                        NotesDao notesDao = MyDatabase.getDatabase(NotesEditActivity.this).notesDao();
                        notesDao.delete(Integer.parseInt(id));
                    });
                    Toast.makeText(NotesEditActivity.this, "Note supprimée", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NotesEditActivity.this, NotesActivity.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(NotesEditActivity.this, "Impossible de supprimer une nouvelle note", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //sauvegarde d'une note
        button_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //verification du titre et texte
                if (String.valueOf(editText_title.getText()).equals("") || String.valueOf(editText_text.getText()).equals(""))
                {
                    Toast.makeText(NotesEditActivity.this, "Titre ou contenu manquant", Toast.LENGTH_SHORT).show();
                    return;
                }
                Executors.newSingleThreadExecutor().execute(() ->
                {
                    NotesDao notesDao = MyDatabase.getDatabase(NotesEditActivity.this).notesDao();
                    //appel de la requete update
                    if (note!=null)
                    {
                        notesDao.update(Integer.parseInt(id),String.valueOf(editText_title.getText()), String.valueOf(editText_text.getText()));
                    }
                    else
                    {
                        notesDao.insert(new Notes(null, city, String.valueOf(editText_title.getText()), String.valueOf(editText_text.getText())));
                    }

                });
                Toast.makeText(NotesEditActivity.this, "Note sauvegardée", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
