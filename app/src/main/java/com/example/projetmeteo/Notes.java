package com.example.projetmeteo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "notes")
public class Notes
{
    @PrimaryKey(autoGenerate = true) private final Integer id;
    private final String city;
    private final String title;
    private final String text;

    public Notes(Integer id, String city, String title, String text)
    {
        this.id = id;
        this.city = city;
        this.title = title;
        this.text = text;
    }

    public Integer getId()
    {
        return id;
    }

    public String getCity()
    {
        return city;
    }

    public String getTitle()
    {
        return title;
    }

    public String getText()
    {
        return text;
    }


    @Override
    public String toString()
    {
        return "Notes{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
