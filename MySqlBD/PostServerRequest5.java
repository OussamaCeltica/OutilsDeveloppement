package com.devs.celtica.inkless;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressListener;


public class PostServerRequest5 {

        /*
     ------------- Utilisation -----------

     //N oublier pas d installer: compile 'com.squareup.okhttp3:okhttp:3.10.0'
     //implementation 'io.github.lizhangqu:coreprogress:1.0.2'

// n oublier pas d ajouter le test de permission pour l acces au storage ..



     -Pour la methode sendWithIMage == destiné au upload des image avec
     qlq data comme un nom specifique ou descreption .. etc pour faire il faut lui
     donné un hashMap contenant des  donné et un arrayList de type Uri contenant des uri récupéré dans
     activityresult d'apres le choix d'image from galery et les envoyé et traité en php comme un tableau
     de fichier avec les methode FILE et pour les data avec POST ..

     -Pour read== il suffit juste d envoyé la requete pour php et récupéré les data dans un string ..





     */

        OkHttpClient client=null;


    public String url;
    private String urlRead="",urlWrite="";
    AppCompatActivity context=null;
    private FileUtils filePath=new FileUtils();
    private ProgressDialog progress;
    public String queryResult="";

    public PostServerRequest5(String url ) {
        this.url = url;

            client = new OkHttpClient.Builder()
                    //pour le temp max d upload s il depasse alors un echec ..
                    .connectTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .build();


    }

    public void setUrlRead(String urlRead) {
        this.urlRead = urlRead;
    }

    public void setUrlWrite(String urlWrite) {
        this.urlWrite = urlWrite;
    }

    /*--------------------- Utilisation de la methode READ -----------------*/
    /*
    elle permet d envouyé une requete sql comme un string de type POST a un serveur (page php ex: read.php), pour specifier
    le fichier qui doit récupérer la requete (le cas ou vous avez plusieurs php files) identifier la page avec setUrlRead(path)
     exemple setUrlRead("/readfile/publication.php");
     dans le fichier publication.php il suffit de faire $_POST['query'] et voila ..

     */
    public void read (final String query, final HashMap<String,String> data, final doBeforAndAfterGettingData d){
        d.before();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                if (client == null){
                     client = new OkHttpClient.Builder()
                            //pour le temp max d upload s il depasse alors un echec ..
                            .connectTimeout(15, TimeUnit.MINUTES)
                            .writeTimeout(15, TimeUnit.MINUTES)
                            .readTimeout(15, TimeUnit.MINUTES)
                            .build();
                }


                MultipartBody.Builder mb = new MultipartBody.Builder();

                RequestBody request_body;
                mb.setType(MultipartBody.FORM);

                for (int i=0;i<data.size();i++){
                    mb.addFormDataPart((i+1)+"",data.get(data.keySet().toArray()[i]));

                }

                mb.addFormDataPart("nbrElem",data.size()+"");
                mb.addFormDataPart("query",query+"");

                request_body=mb.build();

                Request request = new Request.Builder()
                        .url(url+urlRead)

                        .post(request_body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        d.echec(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        queryResult=response.body().string().trim();
                        d.After(queryResult);
                    }
                });

            }

        });

        t.start();
    }

    public void write (final String query, final HashMap<String,String> data, final doBeforAndAfterGettingData d){
        d.before();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                if (client == null){
                    client = new OkHttpClient.Builder()
                            //pour le temp max d upload s il depasse alors un echec ..
                            .connectTimeout(15, TimeUnit.MINUTES)
                            .writeTimeout(15, TimeUnit.MINUTES)
                            .readTimeout(15, TimeUnit.MINUTES)
                            .build();
                }


                MultipartBody.Builder mb = new MultipartBody.Builder();

                RequestBody request_body;
                mb.setType(MultipartBody.FORM);

                for (int i=0;i<data.size();i++){
                    mb.addFormDataPart((i+1)+"",data.get(data.keySet().toArray()[i]));

                }

                mb.addFormDataPart("nbrElem",data.size()+"");
                mb.addFormDataPart("query",query+"");

                request_body=mb.build();

                Request request = new Request.Builder()
                        .url(url+urlRead)

                        .post(request_body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        d.echec(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        queryResult=response.body().string().trim();
                        d.After(queryResult);
                    }
                });

            }

        });

        t.start();
    }


    public void send(final HashMap<String,String> data, final doBeforAndAfterGettingData d){

       d.before();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                if (client == null){
                    client = new OkHttpClient.Builder()
                            //pour le temp max d upload s il depasse alors un echec ..
                            .connectTimeout(15, TimeUnit.MINUTES)
                            .writeTimeout(15, TimeUnit.MINUTES)
                            .readTimeout(15, TimeUnit.MINUTES)
                            .build();
                }


                MultipartBody.Builder mb = new MultipartBody.Builder();

                RequestBody request_body;
                mb.setType(MultipartBody.FORM);

                for (int i=0;i<data.size();i++){
                    mb.addFormDataPart(data.keySet().toArray()[i].toString(),data.get(data.keySet().toArray()[i]));

                }

                request_body=mb.build();

                Request request = new Request.Builder()
                        .url(url+urlWrite)

                        .post(request_body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        d.echec(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {


                       queryResult=response.body().string().trim();
                        d.After(queryResult);

                        /*-------- Manipulation de resultats ----------
                        try {
                           JSONArray datas= new JSONArray(result);
                           for (int i=0;i!=datas.length();i++) {
                           JSONObject obj=datas.getJSONObject(i);
                           //obj.getString("NOmColonne");
                           Log.e("result",obj.getString("num_Clt")+"");
                           }
                        } catch (JSONException e) {
                           e.printStackTrace();
                         }
                         */
                    }
                });

            }

        });

        t.start();

    }


    /*------------- Utilisation de la methodes sendWithFiles --------------------------*/
    /*
    cette methode permet d uploader des fichiers "files" donné sous forme "uri" a un serveur , avec d autres données
    "data" qui seront récupéré dans le php:
    1-les files on la manipule avec La Variable Global $_FILE (Voir documentation php Files)
    2- Data on le récupère avec la methode $_POST et on manipule chacun selon leur clé ..

    PS: le Hashmap contient les données comme clé/valeur , dans php: $valeur=$_POST['clé'];
     */
    public void sendWithFiles(final HashMap<String,String> data, final ArrayList<Uri> files, final AppCompatActivity c , final doBeforAndAfterUpload d){

        context=c;
        d.before();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                String selectedFilePath;
                File f;
                String content_type;
                RequestBody file_body;
                String file_path;



                if (client == null){
                    client = new OkHttpClient.Builder()
                            //pour le temp max d upload s il depasse alors un echec ..
                            .connectTimeout(15, TimeUnit.MINUTES)
                            .writeTimeout(15, TimeUnit.MINUTES)
                            .readTimeout(15, TimeUnit.MINUTES)
                            .build();
                }


                MultipartBody.Builder mb = new MultipartBody.Builder();

                RequestBody request_body;
                mb.setType(MultipartBody.FORM);

                for (int i=0;i<files.size();i++){

                    selectedFilePath =filePath.getRealPath(context,files.get(i));

                    f = new File(selectedFilePath);

                    content_type  = c.getContentResolver().getType(files.get(i));

                    file_body = RequestBody.create(MediaType.parse(content_type),f);

                    file_path = f.getAbsolutePath();
                    mb.addFormDataPart("type","multipart/form-data");
                    mb.addFormDataPart("uploaded_file["+i+"]",file_path.substring(file_path.lastIndexOf("/")+1), file_body);

                }

                mb.addFormDataPart("nbrFile",""+files.size());

                for (int i=0;i<data.size();i++){
                    mb.addFormDataPart(data.keySet().toArray()[i].toString(),data.get(data.keySet().toArray()[i]));
                    Log.e(data.keySet().toArray()[i].toString(),data.get(data.keySet().toArray()[i]));

                }

                request_body=mb.build();

                RequestBody r=ProgressHelper.withProgress(request_body, new ProgressListener() {
                    @Override
                    public void onProgressChanged(final long numBytes, final long totalBytes, final float percent, final float speed) {
                        c.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                d.onProgress(numBytes,totalBytes,percent,speed);
                                Log.e("prr",percent*100+" %");
                            }
                        });

                    }
                });


                Request request = new Request.Builder()
                        .url(url+urlWrite)
                        .post(r)
                        .build();



                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        d.echec(e);

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        queryResult=response.body().string().replaceAll("\n","");
                        d.After(queryResult);
                    }
                });


            }
        });

        t.start();

    }

    public void readPdfFile(final AppCompatActivity c , final doBeforAndAfterGettingFile d){

        context=c;
        d.before();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                String selectedFilePath;
                File f;
                String content_type;
                RequestBody file_body;
                String file_path;


                if (client == null){
                    client = new OkHttpClient.Builder()
                            //pour le temp max d upload s il depasse alors un echec ..
                            .connectTimeout(15, TimeUnit.MINUTES)
                            .writeTimeout(15, TimeUnit.MINUTES)
                            .readTimeout(15, TimeUnit.MINUTES)
                            .build();
                }


                MultipartBody.Builder mb = new MultipartBody.Builder();


                mb.setType(MultipartBody.FORM);

                Request request = new Request.Builder()
                        .url(url+urlRead)
                        .get()
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        d.echec(e);

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        d.After(response.body().byteStream());
                    }
                });


            }
        });

        t.start();

    }



    public interface doBeforAndAfterGettingData {

        void before();
        void echec(Exception e);
        void After(String result);

    }

    public interface doBeforAndAfterGettingFile {

        void before();
        void echec(Exception e);
        void After(InputStream result);

    }

    public interface doBeforAndAfterUpload extends doBeforAndAfterGettingData {
        void onProgress(long numBytes, long totalBytes, float percent, float speed);
    }

    //cette classe permet de récupéré le réel chemin d un fichier depuis un Uri ..
    public class FileUtils {

        public  String getRealPath(Context context, Uri fileUri) {
            String realPath;
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11) {
                realPath =   getRealPathFromURI_BelowAPI11(context, fileUri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19) {
                realPath =  getRealPathFromURI_API11to18(context, fileUri);
            }
            // SDK > 19 (Android 4.4) and up
            else {
                realPath =   getRealPathFromURI_API19(context, fileUri);
            }
            return realPath;
        }


        @SuppressLint("NewApi")
        public  String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
                cursor.close();
            }
            return result;
        }

        public  String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = 0;
            String result = "";
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
                cursor.close();
                return result;
            }
            return result;
        }

        @SuppressLint("NewApi")
        public  String getRealPathFromURI_API19(final Context context, final Uri uri) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    // This is for checking Main Memory
                    if ("primary".equalsIgnoreCase(type)) {
                        if (split.length > 1) {
                            return Environment.getExternalStorageDirectory() + "/" + split[1];
                        } else {
                            return Environment.getExternalStorageDirectory() + "/";
                        }
                        // This is for checking SD Card
                    } else {
                        return "storage" + "/" + docId.replace(":", "/");
                    }

                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    String fileName = getFilePath(context, uri);
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                    }

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        public  String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }


        public  String getFilePath(Context context, Uri uri) {

            Cursor cursor = null;
            final String[] projection = {
                    MediaStore.MediaColumns.DISPLAY_NAME
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public  boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public  boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public  boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public  boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }

}



