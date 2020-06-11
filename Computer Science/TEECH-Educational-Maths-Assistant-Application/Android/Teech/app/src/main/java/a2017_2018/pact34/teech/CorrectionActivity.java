package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import TeechInterface.ClientConnection;
import TeechInterface.Corrige;
import TeechInterface.Exercice;
import katex.hourglass.in.mathlib.MathView;

public class CorrectionActivity extends AppCompatActivity {

    private MathView mathView;
    private int idExo;
    private Corrige correction;
    private CorrectionTask correctionTask;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);
        mathView = findViewById(R.id.MathView2);
        photoView = findViewById(R.id.photo_view_corr);
        boolean passe = getIntent().getBooleanExtra("passe", false);
        idExo = getIntent().getIntExtra("idExo", 0);
        if(passe){
            Intent intent = new Intent(this,FeedbackActivity.class);
            intent.putExtra("passe", true);
            intent.putExtra("idExo", idExo);
            startActivity(intent);
        } else {
            correctionTask = new CorrectionTask();
            correctionTask.execute((Void)null);
        }
    }


    public void goToFeedbackActivity(View view){
        Intent intent = new Intent(this,FeedbackActivity.class);
        intent.putExtra("passe", false);
        intent.putExtra("idExo", idExo);
        startActivity(intent);
    }


    private void render(Corrige corrige) {
        if(corrige.getType().equals("latex")) {
            photoView.setVisibility(View.INVISIBLE);
            String enonce = corrige.getEnonce();
            mathView.setDisplayText(enonce);
        } else {
            mathView.setVisibility(View.INVISIBLE);
            byte[] data = ClientConnection.decodeImage(corrige.getEnonce());
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            photoView.setImageBitmap(bitmap);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class CorrectionTask extends AsyncTask<Void, Void, Boolean> {


        CorrectionTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            correction = Exercice.findCorrige(idExo);
            return(correction!=null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            correctionTask = null;

            if (success) {
                render(correction);
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement de la correction",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            correctionTask = null;
        }
    }


}
