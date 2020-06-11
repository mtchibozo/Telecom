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

public class RegisterTeacherActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmView;
    private EditText etablissementView;
    private EditText villeView;
    private EditText classeView;
    private EditText specView;
    private SessionStockage session;
    private ConnexionTask mConnexTask = null;

    private boolean cancel = false;
    private View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);
        nameView = findViewById(R.id.editTextName);
        emailView = findViewById(R.id.editEmail);
        passwordView = findViewById(R.id.editPassword);
        confirmView = findViewById(R.id.confirmPassword);
        etablissementView = findViewById(R.id.editEtablissement);
        villeView = findViewById(R.id.editVille);
        classeView = findViewById(R.id.editClasse);
        specView = findViewById(R.id.editSpec);
        session = new SessionStockage(getApplicationContext());
    }


    private boolean isEmailValid(String email) {
        //Replace this with your own logic and change the tests in the login activities
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //Replace this with your own logic and change the tests in the login activities
        return password.length() > 4;
    }


    public void finishTeacher(Utilisateur usr) {
        mConnexTask = new ConnexionTask(usr);
        mConnexTask.execute((Void)null);
    }


    public void verifyFieldsTeacher(View v){

        // Reset errors.
        nameView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        confirmView.setError(null);
        etablissementView.setError(null);
        villeView.setError(null);
        classeView.setError(null);
        specView = findViewById(R.id.editSpec);

        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String confirmation = confirmView.getText().toString();
        String etab = etablissementView.getText().toString();
        String ville = villeView.getText().toString();
        String classe = classeView.getText().toString();


        if(TextUtils.isEmpty(specView.getText().toString())){
            specView.setError(getString(R.string.error_field_required));
            focusView = specView;
            cancel = true;
        }

        if(TextUtils.isEmpty(classe)){
            classeView.setError(getString(R.string.error_field_required));
            focusView = classeView;
            cancel = true;
        }

        if(TextUtils.isEmpty(ville)){
            villeView.setError(getString(R.string.error_field_required));
            focusView = villeView;
            cancel = true;
        }

        if(TextUtils.isEmpty(etab)){
            etablissementView.setError(getString(R.string.error_field_required));
            focusView = etablissementView;
            cancel = true;
        }

        // Check if password and password confirmations are equals
        if(TextUtils.isEmpty(confirmation)){
            confirmView.setError(getString(R.string.error_incorrect_confirmation));
            focusView = passwordView;
            cancel = true;
        }
        else if (!confirmation.equals(password)) {
            confirmView.setError(getString(R.string.error_incorrect_confirmation));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password) ) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        //TODO: if UserInterface.isEmailIn(name)
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if(TextUtils.isEmpty(nameView.getText().toString())){
            nameView.setError(getString(R.string.error_field_required));
            focusView = nameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Goes to the next step
            Utilisateur newUser = new Utilisateur(name, "professeur", email, password, etab, ville, classe);
            finishTeacher(newUser);
        }
    }

    private void idPresent(){
        nameView.setError("Identifiant déjà utilisé");
        nameView.requestFocus();
    }


    @SuppressLint("StaticFieldLeak")
    private class ConnexionTask extends AsyncTask<Void, Void, Boolean> {

        private Utilisateur usr;
        boolean testid =false;


        ConnexionTask(Utilisateur usr) {
            this.usr = usr;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                if(Utilisateur.isIdentifiantIn(nameView.getText().toString())){
                    testid = true;
                    return false;
                }
                int id = usr.addUtilisateur();
                usr.setId(id);
            } catch (Exception e){return false;}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mConnexTask = null;

            if (success) {
                session.storeSession(usr);
                Intent intent = new Intent(RegisterTeacherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (testid) {
                    idPresent();
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mConnexTask = null;
        }
    }


}

