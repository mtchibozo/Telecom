package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;

import TeechInterface.Utilisateur;

public class FeedbackActivity extends AppCompatActivity {

    private int idExo;
    private int note;
    private boolean passe;
    int idUser;
    int utilite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        SessionStockage session = new SessionStockage(getApplicationContext());
        idUser = session.pref.getInt(SessionStockage.KEY_ID, 0);
        passe = getIntent().getBooleanExtra("passe", false);
        note=5; //initialisée à 0 et non modifiée s'il a passé
        idExo = getIntent().getIntExtra("idExo", 0);
    }


    private void goToNext(boolean main){
        if(main){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ChooseMatiere.class);
            startActivity(intent);
        }
    }

    public void nextExercise(View view){
        EnvoiTask sender = new EnvoiTask(note, utilite, false);
        sender.execute((Void)null);
    }

    public void goToMain(View v){
        EnvoiTask sender = new EnvoiTask(note, utilite, true);
        sender.execute((Void)null);
    }



    @SuppressLint("StaticFieldLeak")
    private class EnvoiTask extends AsyncTask<Void, Void, Boolean> {

        int eval;
        int util;
        boolean main;

        EnvoiTask(int eval, int util, boolean choix) {
            this.eval = eval;
            this.util = util;
            this.main = choix;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            RatingBar barUtil = findViewById(R.id.Pertinence);
            utilite = Math.round(barUtil.getNumStars() * 2);
            if(!passe){

                RatingBar barEval = findViewById(R.id.Note);
                note = Math.round(barEval.getNumStars() * 2);

                try{Utilisateur.addEval(idUser, idExo, note, utilite, 5);}
                catch(IOException e){return false;}
                return true;

            } else {
                try{Utilisateur.addEval(idUser, idExo, 5, utilite, 5);}
                catch (IOException e){return false;}
                return true;}
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                goToNext(main);
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors de l'envoi des données",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
