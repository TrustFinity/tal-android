package pro.ambrose.www.tal_surveys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
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
    private String GET_DATA_URL = "http://tal-surveys.herokuapp.com/api/v1/get-profile?facebook_id=";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if (Profile.getCurrentProfile() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        this.access_token = Profile.getCurrentProfile().getId();

        profile_image = (ImageView) findViewById(R.id.profile_image);
        logout = (Button) findViewById(R.id.log_out);
        names = (TextView) findViewById(R.id.names);

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

        progressDialog = new ProgressDialog(this);
        new GetUserData(GET_DATA_URL + access_token).execute();
    }

    private void parseData(String raw_json) {
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
