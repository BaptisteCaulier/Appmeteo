package com.example.projetmeteo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    //declaration des variables
    Vector items = new Vector();
    private Button ville;
    private Button pays;
    private Button favoris;
    private Button position;
    private ListView villesListView;
    private ArrayAdapter villesArrayAdapter;

    //declaration des tableaux pour les pays, états et pays
    String[] tableaupays = {"France", "Germany", "USA", "Cyprus", "Spain", "Argentina", "Australia", "Canada", "Cyprus", "Germany", "United Kingdom", "France", "Japan", "China", "Mexico", "Germany", "United Kingdom", "USA", "Spain", "Poland", "Turkey", "Luxembourg", "France", "Cyprus", "South Africa", "Slovenia", "Croatia", "India", "South Korea", "USA", "Australia", "Canada", "Japan", "China", "India", "South Korea"};
    String[] tableauetats = {"Ile-de-France", "Berlin", "California", "Larnaka", "Madrid", "Buenos Aires", "Queensland", "Alberta", "Nicosia", "Bavaria", "England", "Hauts-de-France", "Tokyo", "Beijing", "Mexico City", "Bremen", "Scotland", "New York", "Valencia", "Lublin", "Antalya", "Diekirch", "Nouvelle-Aquitaine", "Pafos", "Limpopo", "Dolenjska", "Zagreb", "Delhi", "Seoul", "Texas", "Victoria", "Quebec", "Akita", "Fujian", "Haryana", "Daegu"};
    String[] tableauvilles = {"Paris", "Berlin", "Los Angeles", "Larnaca", "Madrid", "Buenos Aires", "Brisbane", "Brooks", "Nicosia", "Munich", "London", "Calais", "Tokyo", "Beijing", "Mexico City", "Bremen", "Edinburgh", "New York City", "Valencia", "Lublin", "Antalya", "Diekirch", "Bordeaux", "Paphos", "Lephalale", "Kromberk", "Zagreb", "Delhi", "Seoul", "Dallas", "Melbourne", "Montreal", "Hiyama", "Ximei", "Palwal", "Daegu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //affectation des id des boutons aux variables
        this.ville = findViewById(R.id.ville);
        this.pays = findViewById(R.id.pays);
        this.favoris = findViewById(R.id.favoris);
        this.position = findViewById(R.id.position);

        //affectation de la listview et de l'arrayadapter
        this.villesListView = findViewById(R.id.listView);
        this.villesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        villesListView.setAdapter(villesArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //affichage des villes
        for (int i=0; i<tableauvilles.length; i++)
        {
            items.add(tableauvilles[i] + ", " + tableaupays[i]);
        }

        //action quand on clique sur une ville
        villesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //On affiche l'item cliqué
                Toast.makeText(MainActivity.this, String.valueOf(items.get(i)), Toast.LENGTH_SHORT).show();
            }
        });
    }
}