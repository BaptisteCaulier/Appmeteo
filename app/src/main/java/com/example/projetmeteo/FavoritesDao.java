package com.example.projetmeteo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao
{
    //query to insert a favorite city in the database
    @Insert
    void insert(Favorites... favorites);

    //query to put a city as favorite or not in the database
    @Query("UPDATE favorites SET favorites = :favorites WHERE city = :city")
    void update_favorites(String city, Integer favorites);

    //query to select all the favorites cities
    @Query("SELECT city FROM favorites WHERE favorites = :favorites")
    List<Favorites> getAll(Integer favorites);

    //query to see if a particular city is a favorite city
    @Query("SELECT favorites FROM favorites WHERE city = :city")
    Integer getFavorite(String city);

}