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
    //query to insert a note in the database
    @Insert
    void insert(Notes... notes);

    //query to update an existing note in the database
    @Query("UPDATE notes SET title = :title, text = :text WHERE id = :id")
    void update(int id, String title, String text);

    //query to delete a note
    @Query("DELETE FROM notes WHERE id = :id")
    void delete(int id);

    //query to select all the notes for a particular city
    @Query("SELECT * FROM notes WHERE city = :city")
    List<Notes> getAll(String city);
}
