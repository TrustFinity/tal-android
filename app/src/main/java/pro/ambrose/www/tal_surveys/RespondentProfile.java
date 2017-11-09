package pro.ambrose.www.tal_surveys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import static android.R.attr.defaultValue;

public class RespondentProfile extends AppCompatActivity {

    private static final String TAG = "ProfileTag";
    private ImageView profile_image;
    private Button logout;
    private TextView respondents_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_image = (ImageView) findViewById(R.id.profile_image);
        respondents_name = (TextView) findViewById(R.id.respondents_name);
        logout = (Button) findViewById(R.id.log_out);


        respondents_name.setText(
                Profile.getCurrentProfile().getFirstName()+ " "+
                Profile.getCurrentProfile().getMiddleName()+ " "+
                Profile.getCurrentProfile().getLastName()
        );

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString()).into(profile_image);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });
        Log.d(TAG, Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString());
    }
}
