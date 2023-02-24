package com.example.projetmeteo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class CityActivity extends AppCompatActivity
{
    //declaration des variables
    private String URL;
    private TextView textView_city;
    private TextView textView_date;
    private TextView textView_temperature;
    private TextView textView_wind;
    private TextView textView_air_quality;
    private TextView textView_humidity;
    private TextView textView_pressure;
    private ImageView imageView_icon;
    private Button button_notes;
    private Button button_home;

    //tableaux pour les pays, etats et villes
    String[] countries = {"France", "Germany", "USA", "Spain", "Australia", "Canada", "Cyprus", "Germany", "United Kingdom", "France", "Japan", "China", "Mexico"};
    String[] states = {"Ile-de-France", "Berlin", "California", "Madrid", "Queensland", "Alberta", "Nicosia", "Bavaria", "England", "Hauts-de-France", "Tokyo", "Beijing", "Mexico City"};
    String[] cities = {"Paris", "Berlin", "Los Angeles", "Madrid", "Brisbane", "Brooks", "Nicosia", "Munich", "London", "Calais", "Tokyo", "Beijing", "Mexico City"};
    
    //variable pour recevoir la ville cliquée
    int i = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city);

        //on affecte l'id des éléments aux variables
        this.textView_city = findViewById(R.id.city);
        this.textView_date = findViewById(R.id.date);
        this.textView_temperature = findViewById(R.id.temperature);
        this.textView_wind = findViewById(R.id.wind);
        this.textView_air_quality = findViewById(R.id.air_quality);
        this.textView_humidity = findViewById(R.id.humidity);
        this.textView_pressure = findViewById(R.id.pressure);

        this.imageView_icon = findViewById(R.id.icon);

        this.button_notes = findViewById(R.id.notes);
        this.button_home = findViewById(R.id.home);

        //on change la couleur des boutons
        button_notes.setBackgroundColor(0XFF0072D7);
        button_home.setBackgroundColor(0XFF0072D7);
        button_notes.setVisibility(View.INVISIBLE);
        button_home.setVisibility(View.INVISIBLE);

        //on récupère la ville cliqué dans l'activité d'avant
        Intent intent = getIntent();
        String value = intent.getStringExtra("item");
        //si la valeur est nulle ça veut dire qu'on a cliqué sur le bouton ma position
        if (value!=null)
        {
            //on récupère la ville et le pays
            String sub1 = Objects.requireNonNull(value).substring(0,value.indexOf(","));
            String sub2 = Objects.requireNonNull(value).substring(value.indexOf(","));
            sub2 = sub2.substring(2);

            //on cherche la bonne ville dans le tableau
            if (Arrays.asList(cities).contains(sub1))
            {
                i = Arrays.asList(cities).indexOf(sub1);
            }
            if (Arrays.asList(cities).contains(sub2))
            {
                i = Arrays.asList(cities).indexOf(sub2);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        //ici on regarde si on trouve la ville dans notre tableau
        if (i!=50)
        {
            URL = "https://api.airvisual.com/v2/city?city=" + cities[i] + "&state=" + states[i] + "&country=" + countries[i] + "&key=d60111dd-edf5-4f95-9af7-23a1f9771611";
        }
        //sinon cela veut dire qu'on a appuyé sur le bouton ma position
        else
        {
            URL = "https://api.airvisual.com/v2/nearest_city?key=d60111dd-edf5-4f95-9af7-23a1f9771611";
        }

        //on utilise la bibliothèque Volley pour appeler les données de l'API
        RequestQueue requestQueue = Volley.newRequestQueue(CityActivity.this);

        //on crée un objet JSON pour récupérer les données de l'API sous format JSON
        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try
            {
                JSONObject data = response.getJSONObject("data");
                String city = data.getString("city");//city

                JSONObject current = data.getJSONObject("current");
                JSONObject weather = current.getJSONObject("weather");

                //Affichage de la date au bon format
                String ts = weather.getString("ts");
                ts = ts.replace("T"," ");
                ts = ts.replace("Z","");
                DateTimeFormatter old_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                DateTimeFormatter good_format = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.FRENCH);
                LocalDateTime localDateTime = LocalDateTime.parse(ts, old_format);

                int tp = weather.getInt("tp");//temperature
                int pr = weather.getInt("pr");//pression atmospherqique
                int hu = weather.getInt("hu");//humidité

                //conversion du vent de m/s à km/h
                double ws = weather.getDouble("ws");//vitesse du vent
                ws = 3.6*ws;

                //conversion de la direction du vent de degré à une direction
                int wd = weather.getInt("wd");
                String w_direction;
                if (wd > 22 && wd < 68)
                {
                    w_direction = "NE";
                }
                else if (wd > 68 && wd < 113)
                {
                    w_direction = "E";
                }
                else if (wd > 113 && wd < 158)
                {
                    w_direction = "SE";
                }
                else if (wd > 158 && wd < 203)
                {
                    w_direction = "S";
                }
                else if (wd > 203 && wd < 248)
                {
                    w_direction = "SO";
                }
                else if (wd > 248 && wd < 293)
                {
                    w_direction = "O";
                }
                else if (wd > 293 && wd < 338)
                {
                    w_direction = "NO";
                }
                else
                {
                    w_direction = "N";
                }

                //on affiche le bon icone
                String ic = weather.getString("ic");
                if (!ic.equals("01d") && !ic.equals("01n") && !ic.equals("02d") && !ic.equals("02n") && !ic.equals("03d") && !ic.equals("04d") && !ic.equals("09d") && !ic.equals("10d") && !ic.equals("10n") && !ic.equals("11d") && !ic.equals("13d") && !ic.equals("50d"))
                {
                    ic = "01d";
                }

                JSONObject pollution = current.getJSONObject("pollution");
                int aqius = pollution.getInt("aqius");//air quality index

                //on affiche toutes les infos
                textView_city.setText(city);
                textView_city.setBackgroundColor(0X80FFFFFF);
                textView_date.setText(good_format.format(localDateTime));
                textView_date.setBackgroundColor(0X80FFFFFF);
                textView_temperature.setText(tp + " °C");
                textView_temperature.setBackgroundColor(0X80FFFFFF);
                textView_wind.setText("Vent : " + (int) ws + " km/h, direction : " + w_direction);
                textView_wind.setBackgroundColor(0X80FFFFFF);
                textView_air_quality.setText("Qualité de l'air : " + aqius);
                textView_air_quality.setBackgroundColor(0X80FFFFFF);
                textView_humidity.setText("Humidité : " + hu + "%");
                textView_humidity.setBackgroundColor(0X80FFFFFF);
                textView_pressure.setText("Pression atmosphérique : " + pr + " hPa");
                textView_pressure.setBackgroundColor(0X80FFFFFF);
                imageView_icon.setBackgroundColor(0XB3FFFFFF);
                imageView_icon.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/icon_" + ic, null, getPackageName())));
                button_notes.setVisibility(View.VISIBLE);
                button_home.setVisibility(View.VISIBLE);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(CityActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }
        },
                error -> Toast.makeText(CityActivity.this,"erreur",Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);

        //Le bouton accueil renvoie à la page d'accueil
        button_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CityActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Le bouton notes renvoie à l'activité notes créée prochainement
        button_notes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CityActivity.this, NotesActivity.class);
                intent.putExtra("city", String.valueOf(textView_city.getText()));
                startActivity(intent);
            }
        });
    }

}
