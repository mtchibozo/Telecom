package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import TeechInterface.Matiere;

public class AddMatiere extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int idUsr;
    int index = 6;
    String enonce;
    String type;
    ArrayList<Matiere> categories;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matiere);

        type = getIntent().getStringExtra("type");
        enonce = getIntent().getStringExtra("enonce");
        idUsr = (new SessionStockage(this)).pref.getInt(SessionStockage.KEY_ID, 0);

        spinner = findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        spinner.setVisibility(View.INVISIBLE);
        MatieresTask matieresTask = new MatieresTask();
        matieresTask.execute((Void)null);
    }


    public void  save(ArrayList<Integer> selected, ArrayList<Integer> proportion, ArrayList<Integer> dif){
        Intent intent = new Intent(AddMatiere.this, AddExerciseActivity2.class);
        intent.putExtra("enonce", enonce);
        intent.putExtra("type", type);
        intent.putExtra("idUser", idUsr);
        intent.putExtra("matieres", selected);
        intent.putExtra("proportions", proportion);
        intent.putExtra("difficultés", dif);
        startActivity(intent);
        finish();
    }


    public void matSuppl(View v){
        LinearLayout layout = findViewById(R.id.spinners);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tw = new TextView(this);
        tw.setText(R.string.matiere);
        layout.addView(tw, index, lp);
        index++;

        Spinner sp = new Spinner(this);
        setSpinner(sp);
        layout.addView(sp, index, lp);
        index++;

        TextView tw2 = new TextView(this);
        tw2.setText(R.string.prop);
        layout.addView(tw2, index, lp);
        index++;

        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(et, index, lp);
        index++;

        TextView tw3 = new TextView(this);
        tw3.setText(R.string.diff);
        layout.addView(tw3, index, lp);
        index++;

        SeekBar sb = new SeekBar(this);
        sb.setProgress(0);
        sb.setMax(9);
        layout.addView(sb, index, lp);
        index++;

    }

    public void valider(View v){
        LinearLayout container = findViewById(R.id.spinners);
        ArrayList<Integer> selected = new ArrayList<>();
        ArrayList<Integer> prop = new ArrayList<>();
        ArrayList<Integer> diff = new ArrayList<>();
        boolean error = false;
        View focusView = null;

        for (int i = 0; i < container.getChildCount() - 1 ; i+=6) {

            Spinner sp = (Spinner) container.getChildAt(i + 1);
            int id;
            if(sp.getSelectedItem() == null){
                error = true;
                Toast.makeText(getApplicationContext(), "Selectionnez une matière", Toast.LENGTH_LONG).show();
                focusView = sp;
                break;
            }else {
                id = ((Matiere) sp.getSelectedItem()).getId();

                if (selected.contains(id)) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Selectionnez une matière différente des autres", Toast.LENGTH_LONG).show();
                    focusView = sp;
                    break;
                }
            }

            EditText et = (EditText) container.getChildAt(i + 3);
            int proportion;
            if(et.getText().toString().isEmpty() || !TextUtils.isDigitsOnly(et.getText())) {
                error = true;
                focusView = et;
                et.setError("Entrez un nombre");
                break;
            } else {
                String textProp = et.getText().toString();
                proportion = Integer.parseInt(textProp);
            }
            SeekBar sb = (SeekBar) container.getChildAt(i+5);
            int dur = sb.getProgress() + 1;

            selected.add(id);
            prop.add(proportion);
            diff.add(dur);
        }

        int total = 0;
        for(int k:prop){
            total+=k;
        }

        if(total != 100){
            error = true;
            EditText et = (EditText) container.getChildAt(3);
            et.setError("Le total doit être égal à 100");
            focusView = et;
        }

        if(!error){save(selected, prop, diff);}
        else{focusView.requestFocus();}
    }



    private void setSpinner(Spinner sp){
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
    private class MatieresTask extends AsyncTask<Void, Void, Boolean> {


        MatieresTask() {}

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
                setSpinner(spinner);
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement des Matieres",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
