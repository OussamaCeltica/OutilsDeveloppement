package com.example.celtica.orm_application;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by celtica on 25/03/18.
 */

public class MyBD {

    private static MyBD instance =null;
    public DB db;

    @Database(entities = {/* le je met mes table ex: Product.class,ProduitEnFact.class*/}, version = 0,exportSchema = true)
    public static abstract class DB extends RoomDatabase {
        //here we get our Entity DAO (pour les accées au tables  c a d les requette...)
        //public abstract Product.ProductDAO getPrDAO();
        //public abstract ProduitEnFact.ProduitEnFactDAO getPrFactDao();

    }

    private MyBD(AppCompatActivity c){
       db = Room.databaseBuilder(c, DB.class, "room_bd")
                .allowMainThreadQueries()
                //here we can add migration (si le shéma de bd été changé ..)
                .build();

    }

    public  static MyBD getInstance( AppCompatActivity c){
        if (instance==null) {
            instance = new MyBD(c);
        }
        return instance;
    }

}
