package a2017_2018.pact34.teech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private SessionStockage session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionStockage(getApplicationContext());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        session.checkLogged();
    }

    public void goToMatiereActivity(View view){
        Intent intent = new Intent(this, ChooseMatiere.class);
        intent.putExtra("idusr", session.pref.getInt(SessionStockage.KEY_ID, 0));
        startActivity(intent);
    }

    public void goToExerciseHistoryActivity(View view){
        Intent intent= new Intent(this,ExerciseHistoryActivity.class);
        startActivity(intent);
    }
    public void goToAddExercise(View view){
        Intent intent= new Intent(this,AddExerciseActivity.class);
        startActivity(intent);
    }
    public void goToProfile(View view){
        Intent intent= new Intent(this,ProfilActivity.class);
        startActivity(intent);
    }
    public void goToMyMessagesActivity(View view){
        Intent intent= new Intent(this, MyMessagesActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        session.logout();
    }
}
