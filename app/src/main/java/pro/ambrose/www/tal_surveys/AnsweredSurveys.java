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

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;

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

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new RequestRawJSON(USER_SURVEYS_URL).execute();
            }
        });

        new RequestRawJSON(USER_SURVEYS_URL+"?facebook_id="+this.access_token).execute();

        new_survey_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Snackbar.make(view, "Press start to begin the survey", Snackbar.LENGTH_INDEFINITE).setAction("Start", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent answer_intent = new Intent(getApplicationContext(), AnswerSurvey.class);
//                        answer_intent.putExtra("survey_id", new_suvey_data.get(position).getSurveyId());
//                        startActivity(answer_intent);
//                    }
//                }).show();

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
        JSONArray array = new JSONArray(rawJSON);
        new_suvey_data = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            new_suvey_data.add(new NewSurveyModel(
                    array.getJSONObject(i).getInt("id"),
                    //  array.getJSONObject(i).getJSONObject("survey_question").getInt("id"),
                    12,
                    array.getJSONObject(i).getString("name"),
                    array.getJSONObject(i).getString("description"),
                    "What is your name?"));
            // array.getJSONObject(i).getJSONObject("survey_question").getString("question")));
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
