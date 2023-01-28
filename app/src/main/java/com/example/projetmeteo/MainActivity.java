package com.example.projetmeteo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
{
    //déclaration des variables
    Vector items = new Vector();
    private Button city;
    private Button country;
    private Button favorites;
    private Button location;
    private ListView citiesListView;
    private ArrayAdapter citiesArrayAdapter;

    //tableaux pour les pays, etats et villes
    String[] countries = {"France", "Germany", "USA", "Cyprus", "Spain", "Argentina", "Australia", "Canada", "Cyprus", "Germany", "United Kingdom", "France", "Japan", "China", "Mexico", "Germany", "United Kingdom", "USA", "Spain", "Poland", "Turkey", "Luxembourg", "France", "Cyprus", "South Africa", "Slovenia", "Croatia", "India", "South Korea", "USA", "Australia", "Canada", "Japan", "China", "India", "South Korea"};
    String[] states = {"Ile-de-France", "Berlin", "California", "Larnaka", "Madrid", "Buenos Aires", "Queensland", "Alberta", "Nicosia", "Bavaria", "England", "Hauts-de-France", "Tokyo", "Beijing", "Mexico City", "Bremen", "Scotland", "New York", "Valencia", "Lublin", "Antalya", "Diekirch", "Nouvelle-Aquitaine", "Pafos", "Limpopo", "Dolenjska", "Zagreb", "Delhi", "Seoul", "Texas", "Victoria", "Quebec", "Akita", "Fujian", "Haryana", "Daegu"};
    String[] cities = {"Paris", "Berlin", "Los Angeles", "Larnaca", "Madrid", "Buenos Aires", "Brisbane", "Brooks", "Nicosia", "Munich", "London", "Calais", "Tokyo", "Beijing", "Mexico City", "Bremen", "Edinburgh", "New York City", "Valencia", "Lublin", "Antalya", "Diekirch", "Bordeaux", "Paphos", "Lephalale", "Kromberk", "Zagreb", "Delhi", "Seoul", "Dallas", "Melbourne", "Montreal", "Hiyama", "Ximei", "Palwal", "Daegu"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on affecte l'id des boutons aux variables
        this.city = findViewById(R.id.city);
        this.country = findViewById(R.id.country);
        this.favorites = findViewById(R.id.favorites);
        this.location = findViewById(R.id.location);

        //on change la couleur des boutons
        city.setBackgroundColor(0XFF0072D7);
        country.setBackgroundColor(0XFF0072D7);
        favorites.setBackgroundColor(0XFF0072D7);
        location.setBackgroundColor(0XFF0072D7);

        //on affecte l'id de la listview à une variable et on l'adapte pour afficher une simple liste
        this.citiesListView = findViewById(R.id.listView);
        this.citiesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        citiesListView.setAdapter(citiesArrayAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //on affiche les villes dans la liste
        for (int i = 0; i < cities.length; i++) {
            items.add(cities[i] + ", " + countries[i]);
        }

        //action quand on clique sur une ville
        citiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //on démarre l'activité CityActivity
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                //on garde l'item cliqué en paramètre
                intent.putExtra("item", String.valueOf(items.get(i)));
                startActivity(intent);
            }
        });

        //action quand le bouton tri par ville est cliqué
        city.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //on efface la liste
                citiesListView.setAdapter(null);
                items.clear();
                citiesListView.setAdapter(citiesArrayAdapter);

                //on clone le tableau des villes
                String[] copy = cities.clone();
                Arrays.sort(copy);

                //on crée un tableau pour les clés des villes
                int[] keys = new int[copy.length];

                for (int i = 0; i < copy.length; i++) {
                    int j = 0;
                    //on boucle sur le tableau des villes et quand la valeur est égal à celle de la copie on met la clé correspondante dans le tableau des clés
                    while (!copy[i].equals(cities[j])) {
                        j++;
                    }
                    keys[i] = j;
                }

                //on affiche les villes dans l'ordre alphabétique
                for (int i = 0; i < cities.length; i++) {
                    items.add(copy[i] + ", " + countries[keys[i]]);
                }
            }
        });

        //action quand le bouton tri par pays est cliqué
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on efface la liste
                citiesListView.setAdapter(null);
                items.clear();
                citiesListView.setAdapter(citiesArrayAdapter);

                //on clone le tableau des pays
                String[] copy = countries.clone();
                Arrays.sort(copy);

                String[] tmp = countries.clone();

                //on crée un tableau pour les clés des pays
                int[] keys = new int[copy.length];

                for (int i = 0; i < copy.length; i++) {
                    int j = 0;
                    //n boucle sur le tableau des pays et quand la valeur est égal à celle de la copie on met la clé correspondante dans le tableau des clés
                    while (!copy[i].equals(tmp[j])) {
                        j++;
                    }
                    keys[i] = j;
                    //on met la valeur fait au pays qu'on a déjà visité pour éviter les doublons
                    tmp[j] = "fait";
                }

                //on affiche les pays dans l'ordre alphabétique
                for (int i = 0; i < countries.length; i++) {
                    items.add(copy[i] + ", " + cities[keys[i]]);
                }
            }
        });

        //action quand le bouton ma position est cliqué
        location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //on démarre l'activité CityActivity
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                startActivity(intent);
            }
        });
    }
}