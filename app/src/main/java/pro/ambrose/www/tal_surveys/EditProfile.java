package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.facebook.Profile;

public class EditProfile extends AppCompatActivity {

    private EditText first_name, middle_name, last_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();
        if (Profile.getCurrentProfile() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        first_name.setText(Profile.getCurrentProfile().getFirstName());
        middle_name.setText(Profile.getCurrentProfile().getMiddleName());
        last_name.setText(Profile.getCurrentProfile().getLastName());
    }

    private void initUI() {
        first_name = (EditText) findViewById(R.id.first_name);
        middle_name = (EditText) findViewById(R.id.middle_name);
        last_name = (EditText) findViewById(R.id.last_name);
    }
}
