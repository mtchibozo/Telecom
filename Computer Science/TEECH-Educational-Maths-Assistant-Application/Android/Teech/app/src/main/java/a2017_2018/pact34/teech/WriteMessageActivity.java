package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import TeechInterface.Utilisateur;

public class WriteMessageActivity extends AppCompatActivity {

    private EditText editdestinataire;
    private EditText editmessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);
        editdestinataire = findViewById(R.id.EditDestinataire);
        editmessage = findViewById(R.id.EditMessage);

    }

    public void Send(View view)  // SAVE
    {
        int emetteurId;
        SessionStockage session = new SessionStockage(getApplicationContext());
         emetteurId = session.pref.getInt(SessionStockage.KEY_ID, 4);

        boolean cancel = false;
        View focusView = null;

        String destinataire = editdestinataire.getText().toString();
        String message = editmessage.getText().toString();

        if(TextUtils.isEmpty(destinataire)){
        editdestinataire.setError(getString(R.string.error_field_required));
        focusView = editdestinataire;
        cancel = true;    }

        if(TextUtils.isEmpty(message)){
            editmessage.setError(getString(R.string.error_field_required));
            focusView = editmessage;
            cancel = true;    }

         if (cancel) {
        focusView.requestFocus();
            }


        ConnectionTask mMessageTask  = new ConnectionTask(destinataire,message,emetteurId);
        mMessageTask.execute((Void) null);
    }

@SuppressLint("StaticFieldLeak")
private class ConnectionTask extends AsyncTask<Void, Void, Boolean> {

    private String destinataire;
    private String message;
    private int id;

    ConnectionTask(String destinataire,String message,int id) {
        this.destinataire=destinataire;
        this.message=message;
        this.id=id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Utilisateur Emetteur;
        try{
            Emetteur=new Utilisateur(id);
            Emetteur.sentMessageTo(message,destinataire);
        } catch (Exception e){
            return false;}
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            editdestinataire.setText("");
            editmessage.setText("");
            Toast.makeText(getApplicationContext(), "Message envoyé", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(WriteMessageActivity.this, MyMessagesActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Nous n'avons pas pu récupérer vos messages", Toast.LENGTH_SHORT).show();

        }
    }


}

}

