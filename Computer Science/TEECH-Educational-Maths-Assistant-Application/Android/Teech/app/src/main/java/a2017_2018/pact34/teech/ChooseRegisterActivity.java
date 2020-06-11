package a2017_2018.pact34.teech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_register);
    }

    public void buttonStudent(View v) {
        Intent intent = new Intent(ChooseRegisterActivity.this, RegisterStudentActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonTeacher(View v) {
        Intent intent = new Intent(ChooseRegisterActivity.this, RegisterTeacherActivity.class);
        startActivity(intent);
        finish();
    }
}
