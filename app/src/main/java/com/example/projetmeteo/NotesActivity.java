package com.example.projetmeteo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class NotesActivity extends AppCompatActivity
{
    //déclaration des variables
    private List<String> items = new Vector();
    private List<Notes> temp = new Vector();
    private ListView listView_notes;
    private ArrayAdapter arrayAdapter_notes;
    private TextView textView_notes;
    private Button button_home;
    private Button button_new;
    private String city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);

        //affectation des id aux variables et couleurs des boutons
        this.listView_notes = findViewById(R.id.notes);
        listView_notes.setVisibility(View.INVISIBLE);
        listView_notes.setBackgroundColor(0X80FFFFFF);
        this.arrayAdapter_notes = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView_notes.setAdapter(arrayAdapter_notes);

        this.textView_notes = findViewById(R.id.textview_notes);
        textView_notes.setVisibility(View.INVISIBLE);
        textView_notes.setBackgroundColor(0X80FFFFFF);
        this.button_home = findViewById(R.id.home);
        button_home.setBackgroundColor(0XFF0072D7);
        this.button_new = findViewById(R.id.new_note);
        button_new.setBackgroundColor(0XFF0072D7);

        Intent intent = getIntent();
        this.city = intent.getStringExtra("city");
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        Executors.newSingleThreadExecutor().execute(() ->
        {
            //récupération des notes
            NotesDao notesDao = MyDatabase.getDatabase(this).notesDao();
            temp = notesDao.getAll(city);
            //affichage des notes
            if (temp.size()>0) {
                listView_notes.setVisibility(View.VISIBLE);
                for (int i = 0; i < temp.size(); i++) {
                    items.add(temp.get(i).getTitle());
                }
            }
            else
            {
               textView_notes.setVisibility(View.VISIBLE);
            }
            runOnUiThread(() -> arrayAdapter_notes.notifyDataSetChanged());
        });

        //action quand on clique sur un item
        listView_notes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(NotesActivity.this, NotesEditActivity.class);
                intent.putExtra("note", String.valueOf(temp.get(i)));
                startActivity(intent);
            }
        });

        //bouton accueil
        button_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //ajout d'une note
        button_new.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(NotesActivity.this, NotesEditActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

    }
}
