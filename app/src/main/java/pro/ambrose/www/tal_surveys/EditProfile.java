package pro.ambrose.www.tal_surveys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
