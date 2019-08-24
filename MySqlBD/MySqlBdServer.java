package com.example.celtica.usmbapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Client on 16/04/2018.
 */

public class MySqlBdServer {
    private MyBD bd;
    private MyBD2 bdInsert;
    private String MyUrlBD="";
    public String queryResult="";
/*
je donne le chemin de serveur et pardefaut jai les deux fichier db.php pour lire
 et db2.php pour ecrire
 */
    public MySqlBdServer(String urlBd){

        MyUrlBD=urlBd;
    }

    public void read(String query, doBeforAndAfterGettingData dp) {
        bd=new  MyBD(MyUrlBD,dp);
        bd.execute(query);


    }

    public void write(String query, doBeforAndAfterGettingData dp){

        bdInsert=new  MyBD2(MyUrlBD,dp);
        bdInsert.execute(query);

    }
    /*------------------ l interface doBeforAndAfterGettingData ------------------*/

    /*
     cette interface pour donner la main a l utilisateur d exécuté son code apres ou il a obtien ces
     resultas de la requette sans cracher l UI et la methode After() on la post dans onPostExecute() , et pour
      la methode before() est désitiné quand l utilisateur est en attente de résultat , généralement
      pour afficher une icon de wait et on la met dansPreExecute() ..
     */

    public interface doBeforAndAfterGettingData {

        void before();
        void After() throws JSONException;
    }



    /*------------- la classe async task permet de returné un String comme un JSON .. --------------*/

    public class MyBD extends AsyncTask<String,String,String> {
        String result = "";
        private String MyUrl="";
        private doBeforAndAfterGettingData iHaveMyData;

        public MyBD(String MyUrl,doBeforAndAfterGettingData pp){
            iHaveMyData=pp;
            this.MyUrl=MyUrl;
        }




        @Override
        protected void onPreExecute() {
            iHaveMyData.before();


        }

        //recupération des données
        @Override
        protected String doInBackground(String... params) {

            InputStream isr = null;

            try{

                URL url = new URL(MyUrl+"/dbusmb.php?query="+params[0]);

                URLConnection urlConnection = url.openConnection();

                isr = new BufferedInputStream(urlConnection.getInputStream());}

            catch(Exception e){

                Log.e("log_tag", "Error in http connection " + e.toString());

            }








//convert response to string

            try{

                BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"UTF-8"));

                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }

                isr.close();

                result=sb.toString();
                queryResult=result;

            }

            catch(Exception e){

                Log.e("log_tag", "Error converting result " + e.toString());

            }


            return result;

        }




//parse json data

        protected void onPostExecute(String result2){


            try {
                iHaveMyData.After();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                /*String s = "";

                JSONArray jArray = new JSONArray(result);


                for(int i=0; i<jArray.length();i++){


                    JSONObject json = jArray.getJSONObject(i);


                    s = s +"Name : "+json.getString("nom_Clt");

                }*/





            } catch (Exception e) {


// TODO: handle exception

                Log.e("log_tag", "Error Parsing Data "+e.toString());

            }

        }




    }

/*--------------------- AsyncTask pour insérer des données .. -----------*/

/*si je ne veut pas sécurisé alors dans write je met ex: "query=insert into table (id,nom) values(5,'ouss')
et dans php je récupère avec POST_['query']
 si la sécurité: "code=1&id=1&nom=ouss
  */

    public class MyBD2 extends AsyncTask<String,String,String> {
        String result = "";
        private String MyUrl="";
        private doBeforAndAfterGettingData iHaveMyData;

        public MyBD2(String MyUrl,doBeforAndAfterGettingData pp){
            iHaveMyData=pp;
            this.MyUrl=MyUrl;
        }




        @Override
        protected void onPreExecute() {
            iHaveMyData.before();


        }

        //insertion des données
        @Override
        protected String doInBackground(String... params) {

            InputStream isr = null;

            try{

                URL url = new URL(MyUrl+"/db2.php?"+params[0]);

                URLConnection urlConnection = url.openConnection();

                isr = new BufferedInputStream(urlConnection.getInputStream());}

            catch(Exception e){

                Log.e("log_tag", "Error in http connection " + e.toString());

            }








//convert response to string

            try{

                BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"UTF-8"));

                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }

                isr.close();

                result=sb.toString();
                queryResult=sb.toString();

            }

            catch(Exception e){

                Log.e("log_tag", "Error converting result " + e.toString());

            }


            return result;

        }




//parse json data

        protected void onPostExecute(String result2) {


            try {
                iHaveMyData.After();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                /*String s = "";

                JSONArray jArray = new JSONArray(result);


                for(int i=0; i<jArray.length();i++){


                    JSONObject json = jArray.getJSONObject(i);


                    s = s +"Name : "+json.getString("nom_Clt");

                }*/





            } catch (Exception e) {


// TODO: handle exception

                Log.e("log_tag", "Error Parsing Data "+e.toString());

            }

        }




    }



}
