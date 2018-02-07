package pro.ambrose.www.tal_surveys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RespondentProfile extends AppCompatActivity {

    private static final String TAG = "ProfileTag";
    public String access_token;
    private ImageView profile_image;
    private Button logout;
    private TextView names;
    private String GET_DATA_URL = "http://mytalprofile.com/api/v1/get-profile?facebook_id=";
    private ProgressDialog progressDialog;
    private GoogleSignInAccount account;
    private Profile profile;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        account = GoogleSignIn.getLastSignedInAccount(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mAuth = FirebaseAuth.getInstance();

        profile = Profile.getCurrentProfile();

        if (profile == null && account == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if (profile != null) {
            this.access_token = profile.getId();
        }

        if (account != null){
            this.access_token = account.getId();
        }

        profile_image = (ImageView) findViewById(R.id.profile_image);
        logout = (Button) findViewById(R.id.log_out);
        names = (TextView) findViewById(R.id.names);

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile != null) {
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    });
                }
            }
        });
        if (profile != null) {
            Picasso.with(this).load(profile.getProfilePictureUri(200, 200).toString()).into(profile_image);
        }else if (account != null){
            Picasso.with(this).load(account.getPhotoUrl()).into(profile_image);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
                //finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        new GetUserData(GET_DATA_URL + access_token).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(this, About.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseData(String raw_json) {

        if (raw_json == null){
            Intent no_internet = new Intent(getApplicationContext(), NoInternet.class);
            no_internet.putExtra("activity", "pro.ambrose.www.tal_surveys.RespondentProfile");
            startActivity(no_internet);
            finish();
        }

        try {
            JSONObject jsonObject = new JSONObject(raw_json);
            names.setText(
                    jsonObject.getString("first_name")
                            + " "
                            + (jsonObject.getString("middle_name") == "null" ? "" : jsonObject.getString("middle_name"))
                            + " "
                            + jsonObject.getString("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetUserData extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();
        private String URL;

        private GetUserData(String URL) {
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading profile...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(this.URL)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();
            parseData(s);
        }
    }
}
