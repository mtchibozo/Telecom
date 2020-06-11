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
import java.util.ArrayList;
import TeechInterface.Message;
import TeechInterface.Utilisateur;

public class MyMessagesActivity extends AppCompatActivity {



    public int nbMessage;
    private int emetteurId;
    private LinearLayout ll;
    private LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);
        ll = findViewById(R.id.llmsg);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        SessionStockage session = new SessionStockage(getApplicationContext());
        emetteurId = session.pref.getInt(SessionStockage.KEY_ID, 4);
        Toast.makeText(MyMessagesActivity.this, "Bienvenue sur votre Messagerie", Toast.LENGTH_SHORT).show();
    }

    public void display(View view) {

        ConnectionTask mMessageTask  = new ConnectionTask(emetteurId);
        mMessageTask.execute((Void) null);

    }

    public void goToMessage(int i){
        //on récupère à nouveau le numéro du message pour l'envoyer à la vue suivante, pour le visualiser

        Intent intent = new Intent(this,ReadMessageActivity.class);
        intent.putExtra("receivedMessageNb", i);
        intent.putExtra("Key", "sent");
        startActivity(intent);

    }

    public void goToWriteMessageActivity(View view){
        Intent intent = new Intent(this,WriteMessageActivity.class);
        startActivity(intent);

    }

    public void goToMainActivity(View view){
        Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
    }
    public void goToSentMessagesActivity(View view){
        Intent intent = new Intent(this,SentMessagesActivity.class);
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
            ArrayList<Message> receivedMessageList;
            try {
                Emetteur = new Utilisateur(id);
                receivedMessageList = Emetteur.getReceivedMessages();
                nbMessage = receivedMessageList.size();

            } catch (Exception e){
                return false;}
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                if (nbMessage >0){
                    for (int i = 1; i<nbMessage+1; i++){
                        Button myButton = new Button(MyMessagesActivity.this);
                        myButton.setText(getResources().getString(R.string.msgNo, i));
                        myButton.setLayoutParams(lp);
                        ll.addView(myButton, lp);

                        int index = i-1;
                        myButton.setOnClickListener(v -> goToMessage(index));
                    }


                } else
                {   Button myButton = new Button(MyMessagesActivity.this);
                    myButton.setText(getResources().getString(R.string.noMsg));
                    ll.addView(myButton,lp);
                    // quand on ajoute des boutons dynamiquement, il faut que l'indice de la methode onclick goToMessage soit final, donc on definit index
                }
            }
        }
    }

}
