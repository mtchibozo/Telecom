package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import TeechInterface.Message;
import TeechInterface.Utilisateur;

public class ReadMessageActivity extends AppCompatActivity {

    private int emetteurId;
    private String key;
    private TextView messageview;
    private TextView auteurview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        SessionStockage session = new SessionStockage(getApplicationContext());
        emetteurId = session.pref.getInt(SessionStockage.KEY_ID, 4);
        Intent intent= getIntent();
        key = intent.getStringExtra("Key");

        messageview = findViewById(R.id.textViewMsg);
        auteurview = findViewById(R.id.textViewAuteur);
        loadMessage();


    }

    public void loadMessage(){

        ConnectionTask mMessageTask  = new ConnectionTask(emetteurId,key);
        mMessageTask.execute((Void) null);

    }

    public void goToMyMessages(View view) {
    Intent intent = new Intent(ReadMessageActivity.this,MyMessagesActivity.class);
    startActivity(intent);
    }


    @SuppressLint("StaticFieldLeak")
    private class ConnectionTask extends AsyncTask<Void, Void, Boolean> {

        private String key;
        private int id;
        private String contenu;
        String auteur;

        ConnectionTask(int id,String key) {
              this.id=id;
              this.key=key;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Utilisateur Emetteur;
            ArrayList<Message> sentMessageList;
            ArrayList<Message> receivedMessageList;
            try {
                Emetteur = new Utilisateur(id);

                if (key.equals("sent")){
                    sentMessageList = Emetteur.getSentMessages();
                    Intent intent = getIntent();
                    int i = intent.getIntExtra("SentMessageNb", 0);
                    Message messageALire = sentMessageList.get(i);
                    contenu =messageALire.getContenu();
                    auteur = messageALire.getEmetteur().getIdentifiant();
                }
                if (key.equals("received")){
                    receivedMessageList = Emetteur.getReceivedMessages();
                    Intent intent = getIntent();
                    int i = intent.getIntExtra("receivedMessageNb", 0);
                    Message messageALire = receivedMessageList.get(i);
                    contenu = messageALire.getContenu();
                    auteur = messageALire.getEmetteur().getIdentifiant();
                   }

                } catch (Exception e){return false;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                messageview.setText(contenu);
                auteurview.setText(auteur);
                Toast.makeText(getApplicationContext(), "Message chargé avec succès",Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors du chargement",Toast.LENGTH_SHORT).show();
            }
        }


    }
}
