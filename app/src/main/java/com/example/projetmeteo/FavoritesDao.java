package com.example.projetmeteo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao
{
    //insertion d'un favori
    @Insert
    void insert(Favorites... favorites);

    //mise à jour d'un favori
    @Query("UPDATE favorites SET favorites = :favorites WHERE city = :city")
    void update_favorites(String city, Integer favorites);

    //sélection des favoris
    @Query("SELECT city FROM favorites WHERE favorites = :favorites")
    List<Favorites> getAll(Integer favorites);

    //sélection d'une ville favori
    @Query("SELECT favorites FROM favorites WHERE city = :city")
    Integer getFavorite(String city);

}