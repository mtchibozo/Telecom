package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import TeechInterface.Matiere;

public class ChooseMatiere extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private int idusr;
    Spinner sp;
    ArrayList<Matiere> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_matiere);
        idusr = getIntent().getIntExtra("idusr", 0);
        sp = findViewById(R.id.choixMat);
        sp.setVisibility(View.INVISIBLE);

        Matask mTask = new Matask();
        mTask.execute((Void)null);
    }


    public void goToExercisePageActivity(View v){
        Intent intent = new Intent(this, ExercisePageActivity.class);
        Matiere m = (Matiere) sp.getSelectedItem();
        int idmat = m.getId();
        intent.putExtra("idusr", idusr);
        intent.putExtra("idmat", idmat);
        startActivity(intent);
    }


    private void setSpinner(){
        ArrayAdapter<Matiere> matieres = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        matieres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp.setAdapter(matieres);
        sp.setOnItemSelectedListener(this);
        sp.setVisibility(View.VISIBLE);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
    }

    public void onNothingSelected(AdapterView<?> arg0) {}


    @SuppressLint("StaticFieldLeak")
    private class Matask extends AsyncTask<Void, Void, Boolean> {

        Matask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                categories = Matiere.getAll();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        
        protected void onPostExecute(final Boolean success) {

            if (success) {
                setSpinner();
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des Matieres",Toast.LENGTH_SHORT).show();
            }
        }

    }



}
