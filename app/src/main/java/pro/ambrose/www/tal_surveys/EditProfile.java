package pro.ambrose.www.tal_surveys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = "EditProfile";
    public String access_token;
    private EditText first_name,
            middle_name,
            last_name,
            phone_number,
            email,
            age,
            occupation,
            city,
            country;
    private String firstname,
            middlename,
            lastname,
            phonenumber,
            m_Email,
            m_Age,
            m_Occupation,
            m_City,
            m_Country;
    private Button btn_update;
    private String GET_DATA_URL = "http://mytalprofile.com/api/v1/get-profile?facebook_id=";
    private String UPDATE_DATA_URL = "http://mytalprofile.com/api/v1/update-respondent";
    private ProgressDialog progressDialog;
    private Spinner reward_methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Profile.getCurrentProfile() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        this.access_token = Profile.getCurrentProfile().getId();
        Log.d(TAG, access_token);

        initUI();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initValues();
                new PostUserData(UPDATE_DATA_URL).execute();
            }
        });

        new GetUserData(GET_DATA_URL + access_token).execute();
    }

    private void initUI() {
        first_name = (EditText) findViewById(R.id.first_name);
        middle_name = (EditText) findViewById(R.id.middle_name);
        last_name = (EditText) findViewById(R.id.last_name);

        phone_number = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);
        occupation = (EditText) findViewById(R.id.occupation);
        city = (EditText) findViewById(R.id.city);
        country = (EditText) findViewById(R.id.country);

        btn_update = (Button) findViewById(R.id.update_info);

        progressDialog = new ProgressDialog(this);

        reward_methods = findViewById(R.id.spinner);
    }

    private void initValues() {
        firstname = first_name.getText().toString();
        lastname = last_name.getText().toString();
        middlename = middle_name.getText().toString();
        phonenumber = phone_number.getText().toString();
        m_Age = age.getText().toString();
        m_Email = email.getText().toString();
        m_Occupation = occupation.getText().toString();
        m_City = city.getText().toString();
        m_Country = country.getText().toString();
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reward_methods, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        reward_methods.setAdapter(adapter);
    }

    private void handlePostUserData(String s) {
        Log.d(TAG, s);
        startActivity(new Intent(getApplicationContext(), RespondentProfile.class));
    }

    private void handlePostResponse(String raw_json) {
        parseData(raw_json);
    }

    private void parseData(String raw_json) {
        try {
            JSONObject jsonObject = new JSONObject(raw_json);
            first_name.setText((jsonObject.getString("first_name") == "null" ? "" : jsonObject.getString("first_name")));
            middle_name.setText((jsonObject.getString("middle_name") == "null" ? "" : jsonObject.getString("middle_name")));
            last_name.setText((jsonObject.getString("last_name") == "null" ? "" : jsonObject.getString("last_name")));
            age.setText((jsonObject.getString("age") == "null" ? "" : jsonObject.getString("age")));
            phone_number.setText((jsonObject.getString("phone_number") == "null" ? "" : jsonObject.getString("phone_number")));
            email.setText((jsonObject.getString("email") == "null" ? "" : jsonObject.getString("email")));
            occupation.setText((jsonObject.getString("occupation") == "null" ? "" : jsonObject.getString("occupation")));
            city.setText((jsonObject.getString("city") == "null" ? "" : jsonObject.getString("city")));
            country.setText((jsonObject.getString("country") == "null" ? "" : jsonObject.getString("country")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PostUserData extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();
        private String URL;

        private PostUserData(String URL) {
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Updating data...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody body = new FormBody.Builder()
                    .add("facebook_id", access_token)
                    .add("first_name", firstname)
                    .add("middle_name", middlename)
                    .add("last_name", lastname)
                    .add("age", m_Age)
                    .add("phone_number", phonenumber)
                    .add("email", m_Email)
                    .add("occupation", m_Occupation)
                    .add("city", m_City)
                    .add("country", m_Country)
                    .build();
            Request request = new Request.Builder()
                    .url(this.URL)
                    .put(body)
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
            handlePostUserData(s);
            progressDialog.hide();
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
            progressDialog.setMessage("Populating old data...");
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
            handlePostResponse(s);
        }
    }
}
