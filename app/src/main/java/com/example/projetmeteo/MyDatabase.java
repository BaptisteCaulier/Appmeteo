package com.example.projetmeteo;

import android.content.Context;
import android.provider.SyncStateContract;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {Notes.class, Favorites.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase
{
    public abstract NotesDao notesDao();
    public abstract FavoritesDao favoritesDao();

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(Context context)
    {
        if (INSTANCE==null)
        {
            synchronized (MyDatabase.class)
            {
                if (INSTANCE==null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "MyDatabase").allowMainThreadQueries().addCallback(initCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback initCallback = new Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);
        }
    };

}
