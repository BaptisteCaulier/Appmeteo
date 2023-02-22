package com.example.projetmeteo;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao
{
    //insertion d'une note
    @Insert
    void insert(Notes... notes);

    //mise à jour d'une note
    @Query("UPDATE notes SET title = :title, text = :text WHERE id = :id")
    void update(int id, String title, String text);

    //suppression d'une note
    @Query("DELETE FROM notes WHERE id = :id")
    void delete(int id);

    //sélection des notes d'une ville
    @Query("SELECT * FROM notes WHERE city = :city")
    List<Notes> getAll(String city);
}
