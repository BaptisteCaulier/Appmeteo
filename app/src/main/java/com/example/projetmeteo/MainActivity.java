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

import java.util.Arrays;
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

        //action quand on clique sur le bouton tri par ville
        ville.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //on efface la liste
                villesListView.setAdapter(null);
                items.clear();
                villesListView.setAdapter(villesArrayAdapter);

                //on copie le tableau dans un tableau temporaire
                String[] copy = tableauvilles.clone();
                Arrays.sort(copy);

                //on crée un tableau pour les clés
                int[] keys = new int[copy.length];

                for (int i = 0; i < copy.length; i++)
                {
                    int j = 0;
                    //on boucle sur le tableau des villes et on ajoute la valeur de la ville quand la clé est la même
                    while (!copy[i].equals(tableauvilles[j]))
                    {
                        j++;
                    }
                    keys[i] = j;
                }

                //on affiche la liste triée
                for (int i = 0; i < tableauvilles.length; i++)
                {
                    items.add(copy[i] + ", " + tableaupays[keys[i]]);
                }
            }
        });

        //action quand on clique sur le bouton tri par pays
        pays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //on efface la liste
                villesListView.setAdapter(null);
                items.clear();
                villesListView.setAdapter(villesArrayAdapter);

                //on copie le tableau dans un tableau temporaire
                String[] copy = tableaupays.clone();
                Arrays.sort(copy);

                String[] tmp = tableaupays.clone();

                //on crée un tableau pour les clés
                int[] keys = new int[copy.length];

                for (int i = 0; i < copy.length; i++)
                {
                    int j = 0;
                    //on boucle sur le tableau des pays et on ajoute la valeur du pays quand la clé est la même
                    while (!copy[i].equals(tmp[j]))
                    {
                        j++;
                    }
                    keys[i] = j;
                    //on met la valeur done quand on a fini le pays pour éviter les doubles
                    tmp[j] = "done";
                }

                //on affiche la liste triée
                for (int i = 0; i < tableaupays.length; i++)
                {
                    items.add(copy[i] + ", " + tableauvilles[keys[i]]);
                }
            }
        });
    }
}