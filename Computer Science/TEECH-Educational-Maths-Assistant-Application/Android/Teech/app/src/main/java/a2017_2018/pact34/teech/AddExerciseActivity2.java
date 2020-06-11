package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import TeechInterface.ClientConnection;
import TeechInterface.Corrige;
import TeechInterface.Exercice;
import TeechInterface.Matiere;

public class AddExerciseActivity2 extends AppCompatActivity {

    int idUsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise2);
    }


    public void save(String correction, String typeCorr) {

        String type = getIntent().getStringExtra("type");
        String enonce = getIntent().getStringExtra("enonce");
        idUsr = getIntent().getIntExtra("idUser", -1);
        EditText et = findViewById(R.id.Indics);
        String indic = et.getText().toString();
        if(TextUtils.isEmpty(indic)){
            indic = "Pas d'indications pour cet exercice";
        }
        ArrayList<Integer> idMatieres = getIntent().getIntegerArrayListExtra("matieres");
        ArrayList<Integer> prop = getIntent().getIntegerArrayListExtra("proportions");
        ArrayList<Integer> diff = getIntent().getIntegerArrayListExtra("difficultés");


        AddExoTask task = new AddExoTask(type, enonce, idUsr, 0, correction, typeCorr, idMatieres, prop, diff, indic);
        task.execute((Void)null);
    }


    public void returnToMain(View view){
        Intent intent = new Intent(AddExerciseActivity2.this, MainActivity.class);
        startActivity(intent);
        finish();
        }



    public void corrLatex(View v){
        EditText editCorrection = findViewById(R.id.EditCorrection);
        String correction  = editCorrection.getText().toString();
        if(!TextUtils.isEmpty(correction)){
            save(correction, "latex");
        }
    }


    public void corrPic(View v){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            try {
                InputStream iStream = null;
                if (selectedImage != null) {
                    iStream = getContentResolver().openInputStream(selectedImage);
                }
                byte[] image = ProfilActivity.getBytes(iStream);
                String codeImage = ClientConnection.encodeImage(image);
                if(idUsr != -1){
                    save(codeImage, "photo");
                }

            } catch(IOException e){
                Toast.makeText(this, "Erreur lors du chargement de l'image",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AddExoTask extends AsyncTask<Void, Void, Boolean> {

        String type;
        String enonce;
        int usrid;
        int duree;
        String correction;
        String typeCorr;
        ArrayList<Integer> idMat;
        ArrayList<Integer> prop;
        ArrayList<Integer> diff;
        String indication;

        AddExoTask(String type, String enonce, int id, int duree, String correction, String typeCorr, ArrayList<Integer> idM, ArrayList<Integer> prop, ArrayList<Integer> diff, String ind) {
            this.type=type;
            this.enonce=enonce;
            this.usrid=id;
            this.duree=duree;
            this.correction=correction;
            this.typeCorr=typeCorr;
            this.idMat = idM;
            this.prop = prop;
            this.diff = diff;
            indication=ind;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                int idExo = Exercice.add("titre", 5, enonce, 5, usrid, duree, type);
                Corrige.add(idExo, "description", indication, 5, correction, idUsr, typeCorr);
                for(int k=0; k < idMat.size(); k++){
                    Exercice.addMatiere(idExo, new Matiere(idMat.get(k)), prop.get(k), diff.get(k));
                }
            } catch (Exception e){return false;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Intent intent = new Intent(AddExerciseActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
