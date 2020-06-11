package a2017_2018.pact34.teech;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import TeechInterface.ClientConnection;



public class AddExerciseActivity extends AppCompatActivity  {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        SessionStockage session = new SessionStockage(getApplicationContext());
        id = session.pref.getInt(SessionStockage.KEY_ID, -1);
    }


    public void  save(String enonce, String type){
        Intent intent = new Intent(AddExerciseActivity.this, AddMatiere.class);
        intent.putExtra("enonce", enonce);
        intent.putExtra("type", type);
        intent.putExtra("idUser", id);
        startActivity(intent);
        finish();
    }

    public void returnToMain(View view){
        Intent intent = new Intent(AddExerciseActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void ExoLatex(View v){
        EditText editEnonce = findViewById(R.id.EditEnonce);
        String enonce  = editEnonce.getText().toString();
        if(!TextUtils.isEmpty(enonce)){
            save(enonce, "latex");
        }
    }


    public void ExoPic(View v){
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
                if(id != -1){
                    save(codeImage, "photo");
                }

            } catch(IOException e){
                Toast.makeText(this, "Erreur lors du chargement de l'image",Toast.LENGTH_SHORT).show();
            }
        }

    }




}
