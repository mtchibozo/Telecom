package a2017_2018.pact34.teech;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import java.io.IOException;
import java.util.ArrayList;
import TeechInterface.ClientConnection;
import TeechInterface.Commentaire;
import TeechInterface.Corrige;
import TeechInterface.Exercice;
import TeechInterface.Matiere;
import TeechInterface.Utilisateur;
//from https://github.com/lingarajsankaravelu/Katex
import katex.hourglass.in.mathlib.MathView;


public class ExercisePageActivity extends AppCompatActivity {

    private MathView mathView;
    private PhotoView photoView;
    private int idusr;
    private Exercice exo;
    private String ind = "erreur lors du chergement de l'exercice pour les indications";
    private String latest = "TITRE: exercice\nCalculer $\\sum_{k=1}^{n} k^2$TITRE: exercice\nCalculer $\\sum_{k=1}^{n} k^2$TITRE: exercice\nCalculer $\\sum_{k=1}^{n} k^2$TITRE: exercice\nCalculer $\\sum_{k=1}^{n} k^2$";
    private AskExerciceTask exoTask = null;
    ArrayList<Commentaire> coms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);
        SessionStockage session = new SessionStockage(getApplicationContext());
        idusr = session.pref.getInt(SessionStockage.KEY_ID, 0);
        int idexo = getIntent().getIntExtra("id", -1);
        int idmat = getIntent().getIntExtra("idmat", -1);
        mathView = findViewById(R.id.MathView);
        photoView = findViewById(R.id.photo_view_exo);
        latest = latest.replace("\n", " \\newline " );
        exoTask = new AskExerciceTask(idexo, idmat);
        exoTask.execute((Void)null);
    }


    public void goToCorrectionActivity(View view){
        Intent intent = new Intent(this, CorrectionActivity.class);
        intent.putExtra("passe", false);
        intent.putExtra("idExo", exo.getId());
        startActivity(intent);
    }

    private void render(Exercice exo) {
        if(exo.getType().equals("latex")) {
            photoView.setVisibility(View.INVISIBLE);
            String enonce = exo.getEnonce();
            mathView.setDisplayText(enonce);
        } else {
            mathView.setVisibility(View.INVISIBLE);
            byte[] data = ClientConnection.decodeImage(exo.getEnonce());
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            photoView.setImageBitmap(bitmap);
        }
    }

    public void passe(View view){
        Intent intent = new Intent(this, CorrectionActivity.class);
        intent.putExtra("passe", true);
        intent.putExtra("idExo", exo.getId());
        startActivity(intent);
    }

    public void comment(View v){
        EditText et = findViewById(R.id.Commentaire);
        String text = et.getText().toString();
        if(!text.isEmpty()) {
            CommentTask ct = new CommentTask(text);
            ct.execute((Void) null);
        }
    }

    public void indic(View v){
        AlertDialog aide = new AlertDialog.Builder(ExercisePageActivity.this).create();
        aide.setTitle("Indications");
        aide.setMessage(ind);
        aide.show();
    }


    @SuppressLint("StaticFieldLeak")
    private class AskExerciceTask extends AsyncTask<Void, Void, Boolean> {

        int idexo;
        int idmat;

        AskExerciceTask(int idexo, int idmat) {
            this.idexo=idexo;
            this.idmat = idmat;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(idexo==-1) {
                try {
                    if(idmat != -1) {
                        Matiere mtr = new Matiere(idmat);
                        Utilisateur usr = new Utilisateur(idusr);
                        exo = usr.findExercice(mtr, 5);
                    } else {return false;}
                } catch (Exception e) {return false;}
            } else {
                try {exo = new Exercice(idexo);}
                catch (IOException e) {return false;}
            }
            try{
                Corrige corr = exo.findCorrige();
                ind = corr.getIndication();

                coms = Commentaire.getAllCommentsOn(idexo);
            } catch (IOException e) {return true;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            exoTask = null;
            if (success) {
                LinearLayout ll = findViewById(R.id.Comments);
                for(Commentaire c : coms){
                    TextView tv = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tv.setText(c.getContenu());
                    ll.addView(tv, lp);
                }
                render(exo);
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement d'exercice",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            exoTask = null;
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class CommentTask extends AsyncTask<Void, Void, Boolean> {

        String text;

        CommentTask(String s){text=s;}

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Commentaire.add(idusr, text, exo.getId(), 0);
                return true;
            } catch (IOException e){return false;}
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(!success){
                Toast.makeText(getApplicationContext(), "Erreur lors de l'envoi",Toast.LENGTH_SHORT).show();
            } else {
                LinearLayout ll = findViewById(R.id.Comments);
                TextView tv = new TextView(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv.setText(text);
                ll.addView(tv, lp);
            }
        }

    }


}


