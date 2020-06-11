package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import TeechInterface.Utilisateur;


public class ExerciseHistoryActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> listExos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);
        SessionStockage session = new SessionStockage(getApplicationContext());
        int id = session.pref.getInt(SessionStockage.KEY_ID, -1);
        ListTask listask = new ListTask(id);
        listask.execute((Void)null);
    }

    private void createButtons(){
        if(!(listExos == null)){
            LinearLayout layout = findViewById(R.id.layout_history);
            int nb = listExos.size();
            for(int i=0; i<nb; i++) {
                Button but = new Button(this);
                String text = "Exercice " + listExos.get(i);
                but.setTag(Integer.parseInt(listExos.get(i).split(" du ")[0]));
                but.setText(text);
                but.setOnClickListener(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.addView(but, lp);
            }
        }
    }

    public void onClick(View v){
        Button b = (Button) v;
        int id = (int) b.getTag();
        Intent intent= new Intent(this, ExercisePageActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }



    @SuppressLint("StaticFieldLeak")
    private class ListTask extends AsyncTask<Void, Void, Boolean> {

        int idUsr;

        ListTask(int idUsr) {
            this.idUsr = idUsr;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {listExos = Utilisateur.getExercicesDone(idUsr);}
            catch(IOException e){return false;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                createButtons();
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors de la réception des données",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
