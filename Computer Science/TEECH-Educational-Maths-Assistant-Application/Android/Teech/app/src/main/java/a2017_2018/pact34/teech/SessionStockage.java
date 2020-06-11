package a2017_2018.pact34.teech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import TeechInterface.ClientConnection;
import TeechInterface.Utilisateur;

/**
 * Created by louison on 12/03/18.
 * from https://www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
 */

public class SessionStockage {

    SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_IDENTIFIANT = "identifiant";
    // Email address
    public static final String KEY_EMAIL = "email";
    // id
    public static final String KEY_ID = "id";
    //image
    public static final String KEY_IMAGE = "image";
    //satut
    public static final String KEY_STATUT = "statut";
    //niveau
    public static final String KEY_NIVEAU = "niveau";
    //description
    public static final String KEY_DESCRIPTION = "description";
    //etablissement
    public static final String KEY_ETAB = "etab";
    //classe
    public static final String KEY_CLASSE = "classe";
    //ville
    public static final String KEY_VILLE = "ville";


    // Constructor
    public SessionStockage(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences("SESSION", Context.MODE_PRIVATE); //only the app has the access
        editor = pref.edit();
    }


    public void storeSession(Utilisateur usr){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_IDENTIFIANT, usr.getIdentifiant());
        editor.putInt(KEY_ID, usr.getId());
        if(usr.getPhotoDeProfil() != null){
            editor.putString(KEY_IMAGE, ClientConnection.encodeImage(usr.getPhotoDeProfil()));
        }
        editor.putString(KEY_EMAIL, usr.getEmail());
        editor.putString(KEY_STATUT, usr.getStatut());
        editor.putString(KEY_NIVEAU, usr.getNiveau());
        editor.putString(KEY_DESCRIPTION, usr.getDescription());
        editor.putString(KEY_ETAB, usr.getEtablissement());
        editor.putString(KEY_CLASSE, usr.getClasse());
        editor.putString(KEY_VILLE, usr.getVille());

        editor.commit();
    }

    private boolean isLogged(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogged(){
        if(!isLogged()){
            Intent i = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }
    }


    public void logout(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public void modifyPic(String imageCode){
        editor.putString(KEY_IMAGE, imageCode);
        editor.commit();
    }

}
