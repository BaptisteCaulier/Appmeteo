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
    String[] countries = {"France", "Germany", "USA", "Spain", "Australia", "Canada", "Cyprus", "Germany", "United Kingdom", "France", "Japan", "China", "Mexico"};
    String[] states = {"Ile-de-France", "Berlin", "California", "Madrid", "Queensland", "Alberta", "Nicosia", "Bavaria", "England", "Hauts-de-France", "Tokyo", "Beijing", "Mexico City"};
    String[] cities = {"Paris", "Berlin", "Los Angeles", "Madrid", "Brisbane", "Brooks", "Nicosia", "Munich", "London", "Calais", "Tokyo", "Beijing", "Mexico City"};

    public void showToast(final String toast)
    {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show());
    }

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

        //insertion des villes dans la table favoris
        Executors.newSingleThreadExecutor().execute(() ->
        {
            FavoritesDao favoritesDao = MyDatabase.getDatabase(MainActivity.this).favoritesDao();
            List<Favorites> favoritesList = favoritesDao.getAll(0);
            //on vérifie que les villes ne sont pas dans la table
            if (favoritesList.isEmpty())
            {
                for (int i = 0; i < cities.length; i++)
                {
                    //on insère les villes dans la table favoris
                    favoritesDao.insert(new Favorites(null,cities[i],0));
                }
            }
            runOnUiThread(() -> citiesArrayAdapter.notifyDataSetChanged());
        });
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

        //Action quand on reste appuyé sur une ville
        citiesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //when we long click on an item, it adds or removes it from the favorites
                Executors.newSingleThreadExecutor().execute(() ->
                {
                    //we get the item clicked
                    String temp = String.valueOf(items.get(i));
                    for (int k = 0; k < cities.length; k++)
                    {
                        //as the item contains the country and the city, we loop over the city array to get only the city
                        if (temp.contains(cities[k]))
                        {
                            temp=cities[k];
                            break;
                        }
                    }

                    FavoritesDao favoritesDao = MyDatabase.getDatabase(MainActivity.this).favoritesDao();
                    //here we add or remove the city from the favorites
                    if (favoritesDao.getFavorite(temp)==0)
                    {
                        favoritesDao.update_favorites(temp,1);
                        showToast("Ville ajoutée aux favories");
                    }
                    else
                    {
                        favoritesDao.update_favorites(temp,0);
                        showToast("Ville enlevée des favoris");
                    }

                    runOnUiThread(() -> citiesArrayAdapter.notifyDataSetChanged());
                });
                return true;
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

        //action quand on appuie sur favoris
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citiesListView.setAdapter(null);
                items.clear();
                citiesListView.setAdapter(citiesArrayAdapter);

                Executors.newSingleThreadExecutor().execute(() ->
                {
                    //on recupere les villes favories
                    FavoritesDao favoritesDao = MyDatabase.getDatabase(MainActivity.this).favoritesDao();
                    List<Favorites> temp = favoritesDao.getAll(1);
                    if (temp.isEmpty())
                    {
                        showToast("Pas de favoris, rester appuyé sur une ville pour l'ajouter aux favoris");
                    }
                    else
                    {
                        for (int i = 0; i < temp.size(); i++)
                        {
                            for (int j = 0; j < countries.length; j++)
                            {
                                //on affiche
                                if (cities[j].equals(temp.get(i).getCity()))
                                {
                                    items.add(temp.get(i).getCity() + ", " + countries[j]);
                                    break;
                                }
                            }
                        }
                    }
                    runOnUiThread(() -> citiesArrayAdapter.notifyDataSetChanged());
                });
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