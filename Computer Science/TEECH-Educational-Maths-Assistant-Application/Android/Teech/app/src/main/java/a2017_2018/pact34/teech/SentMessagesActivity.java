package a2017_2018.pact34.teech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import TeechInterface.Message;
import TeechInterface.Utilisateur;

public class SentMessagesActivity extends AppCompatActivity {

    private int nbMessage;
    private int emetteurId;
    LinearLayout linear1;
    LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_messages);
        linear1 = findViewById(R.id.linear1);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        SessionStockage session = new SessionStockage(getApplicationContext());
        emetteurId = session.pref.getInt(SessionStockage.KEY_ID, 4);
        display();
    }

    public void display(){
        ConnectionTask mMessageTask  = new ConnectionTask(emetteurId);
        mMessageTask.execute((Void) null);
    }

    public void goToMessage(int i){
        //on récupère à nouveau le message pour l'envoyer à la vue suivante, pour le visualiser
        Intent intent = new Intent(SentMessagesActivity.this,ReadMessageActivity.class);
        intent.putExtra("SentMessageNb", i);
        intent.putExtra("Key", "sent");
        startActivity(intent);

    }

    public void goToMyMessagesActivity(View view){
        Intent intent = new Intent(SentMessagesActivity.this,MyMessagesActivity.class);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private class ConnectionTask extends AsyncTask<Void, Void, Boolean> {

        private int id;

        ConnectionTask(int id) {
                  this.id=id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Utilisateur Emetteur;
            ArrayList<Message> sentMessageList;
            try{
                Emetteur=new Utilisateur(id);
                sentMessageList = Emetteur.getSentMessages();
                nbMessage = sentMessageList.size();
            } catch (Exception e) {
                return false;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
        //      Toast.makeText(getApplicationContext(), "Messages récupérés avec succès", Toast.LENGTH_SHORT).show();
                if (nbMessage>0) {
                    for (int i = 1; i<nbMessage+1; i++){
                        Button myButton = new Button(getApplicationContext());
                        myButton.setText(getResources().getString(R.string.msgNo, i));
                        myButton.setLayoutParams(lp);
                        linear1.addView(myButton, lp);

                        int index = i-1;
                        myButton.setOnClickListener(v -> goToMessage(index));

                    }
// quand on ajoute des boutons dynamiquement, il faut que l'indice de la methode onclick goToMessage soit final, donc on definit index
                }
            }
        }


    }
}

