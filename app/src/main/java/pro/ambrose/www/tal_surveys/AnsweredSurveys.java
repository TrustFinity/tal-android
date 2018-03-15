package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import DataModels.ModelAdapters.NewSurveysAdapter;
import DataModels.NewSurveyModel;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnsweredSurveys extends AppCompatActivity {

    public final String USER_SURVEYS_URL = "http://mytalprofile.com/api/v1/all-surveys";
    ListView new_survey_list;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private ArrayList<NewSurveyModel> new_suvey_data;
    private String TAG = "Answered: ";
    private GoogleSignInAccount account;
    private Profile profile;
    private String access_token;
    private TextView no_surveys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_surveys);
        new_survey_list = (ListView) findViewById(R.id.new_surveys);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        account = GoogleSignIn.getLastSignedInAccount(this);
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

        Log.d(TAG, this.access_token);

        no_surveys = (TextView) findViewById(R.id.no_surveys);
        no_surveys.setVisibility(View.INVISIBLE);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new RequestRawJSON(USER_SURVEYS_URL+"?facebook_id="+access_token).execute();
            }
        });

        new RequestRawJSON(USER_SURVEYS_URL+"?facebook_id="+this.access_token).execute();

        new_survey_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar.make(view, "You have already taken this survey.", Snackbar.LENGTH_INDEFINITE).setAction("Back",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(AnsweredSurveys.this, Home.class));
                            }
                        }).show();
            }
        });

    }

    public void updateUI(String rawJSON) throws JSONException {
        new_suvey_data = new ArrayList<>();

        if (rawJSON == null){
            Intent no_internet = new Intent(getApplicationContext(), AnsweredSurveys.class);
            no_internet.putExtra("activity", "pro.ambrose.www.tal_surveys.RespondentProfile");
            startActivity(no_internet);
            finish();
        }

        if (rawJSON != null){
            JSONArray array = new JSONArray(rawJSON);

            if (array.length() == 0) {
                no_surveys.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < array.length(); i++) {

                // JSONObject survey = array.getJSONObject(i).getJSONObject("survey");
                JSONObject survey = array.getJSONObject(i);

                new_suvey_data.add(new NewSurveyModel(
                        survey.getInt("id"),
                        12,
                        survey.getString("name"),
                        survey.getString("description"),
                        "What is your name?"));
            }
        }
        new_survey_list.setAdapter(new NewSurveysAdapter(new_suvey_data, getApplicationContext()));
    }

    public class RequestRawJSON extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();
        private String URL;

        public RequestRawJSON(String URL) {
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(this.URL)
                    .build();
            Response response ;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(s);
            try {
                updateUI(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
