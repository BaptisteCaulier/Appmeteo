package com.example.projetmeteo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorites
{
    @PrimaryKey(autoGenerate = true) private final Integer id;
    private final String city;
    private final Integer favorites;

    public Favorites(Integer id, String city, Integer favorites)
    {
        this.id = id;
        this.city = city;
        this.favorites = favorites;
    }

    public Integer getId()
    {
        return id;
    }

    public String getCity()
    {
        return city;
    }

    public Integer getFavorites()
    {
        return favorites;
    }
}
