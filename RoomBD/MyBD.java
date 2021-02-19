package com.celtica.doctor.BD;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by celtica on 25/03/18.
 */

public class MyBD {

    private static MyBD instance =null;
    public DB db;

    @Database(entities = {User.class/* le je met mes table ex: Product.class,ProduitEnFact.class*/}, version =1,exportSchema = true)
    public static abstract class DB extends RoomDatabase {
        //here we get our Entity DAO (pour les accées au tables  c a d les requette...)
        //public abstract Product.ProductDAO getPrDAO();
        //public abstract ProduitEnFact.ProduitEnFactDAO getPrFactDao();

        public abstract User.UserDAO getUserDAO();

    }

    private MyBD(Context c){
       db = Room.databaseBuilder(c, DB.class, "room_bd")
                .allowMainThreadQueries()
                //here we can add migration (si le shéma de bd été changé ..)
                .build();

    }

    public  static MyBD getInstance(Context c){
        if (instance==null) {
            instance = new MyBD(c);
        }
        return instance;
    }

}
