package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import TeechInterface.ClientConnection;
import TeechInterface.Utilisateur;

public class ProfilActivity extends AppCompatActivity {

    private SessionStockage session;
    private ImageView imView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        LinearLayout layout = findViewById(R.id.layout_profil);
        imView = findViewById(R.id.imageView3);
        imView.setVisibility(View.INVISIBLE);
        session = new SessionStockage(getApplicationContext());
        Resources r = getResources();
        String defvalue = "error";
        render();

        TextView identifiantText = new TextView(this);
        identifiantText.setText(r.getString(R.string.profil_identifiant, session.pref.getString(SessionStockage.KEY_IDENTIFIANT, defvalue)));
        identifiantText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(identifiantText, lp);

        TextView emailText = new TextView(this);
        emailText.setText(r.getString(R.string.profil_email, session.pref.getString(SessionStockage.KEY_EMAIL, defvalue)));
        emailText.setGravity(Gravity.CENTER);
        layout.addView(emailText, lp);

        TextView statutText = new TextView(this);
        statutText.setText(r.getString(R.string.profil_statut, session.pref.getString(SessionStockage.KEY_STATUT, defvalue)));
        statutText.setGravity(Gravity.CENTER);
        layout.addView(statutText, lp);


        /*
        TextView desciText = new TextView(this);
        desciText.setText("description :" +session.pref.getString(SessionStockage.KEY_DESCRIPTION, "pas marché"));
        desciText.setGravity(Gravity.CENTER);
        layout.addView(desciText, lp);
        */


        TextView villeText = new TextView(this);
        villeText.setText(r.getString(R.string.profil_etab, session.pref.getString(SessionStockage.KEY_ETAB, defvalue)));
        villeText.setGravity(Gravity.CENTER);
        layout.addView(villeText, lp);

        TextView etabText = new TextView(this);
        etabText.setText(r.getString(R.string.profil_ville, session.pref.getString(SessionStockage.KEY_VILLE, defvalue)));
        etabText.setGravity(Gravity.CENTER);
        layout.addView(etabText, lp);
/*
        TextView classText = new TextView(this);
        classText.setText(r.getString(R.string.profil_classe, session.pref.getString(SessionStockage.KEY_CLASSE, defvalue)));
        classText.setGravity(Gravity.CENTER);
        layout.addView(classText, lp);
 */
    }


    public void sendPic(View v){
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
                byte[] image = getBytes(iStream);
                String codeImage = ClientConnection.encodeImage(image);
                int id = session.pref.getInt(SessionStockage.KEY_ID, -1);
                if(id != -1){
                   ImageTask mImgTask = new ImageTask(codeImage, id);
                   mImgTask.execute((Void)null);
                }

            } catch(IOException e){
                Toast.makeText(this, "Erreur lors du chargement de l'image",Toast.LENGTH_SHORT).show();
            }
        }
    }



    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    public void render(){
        String codeImg = session.pref.getString(SessionStockage.KEY_IMAGE, "default");
        if(!codeImg.equals("default")) {
            byte[] photo = ClientConnection.decodeImage(codeImg);
            Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            imView.setImageBitmap(bmp);
            imView.setVisibility(View.VISIBLE);
        }
    }



    @SuppressLint("StaticFieldLeak")
    private class ImageTask extends AsyncTask<Void, Void, Boolean> {

        String code;
        int id;

        ImageTask(String code, int id) {
            this.code=code;
            this.id=id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Utilisateur.modifyPhotoDeProfil(code, id);
            } catch (Exception e){return false;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Log.d("login", "success");
                session.modifyPic(code);
                render();
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement de l'image",Toast.LENGTH_SHORT).show();
                Log.d("login", "failure");
            }
        }
    }


}
