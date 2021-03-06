package pro.ambrose.www.tal_surveys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";
    public final String USER_SURVEYS_URL = "http://mytalprofile.com/api/v1/get-for-user";
    public boolean isConnected = false;
    private TextView mTextMessage, mAboutUs, no_surveys;
    private ListView new_survey_list;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private ArrayList<NewSurveyModel> new_suvey_data;
    private ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    mTextMessage.setText(R.string.title_surveys);
                    return true;
                case R.id.navigation_surveys:
                    mTextMessage.setText(R.string.title_surveys);
                    startActivity(new Intent(getApplicationContext(), AnsweredSurveys.class));
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    startActivity(new Intent(getApplicationContext(), RespondentProfile.class));
                    return true;
            }
            return false;
        }

    };
    private Profile profile;
    private GoogleSignInAccount account;
    private String access_token;

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        profile = Profile.getCurrentProfile();
        account = GoogleSignIn.getLastSignedInAccount(this);

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

        mTextMessage = (TextView) findViewById(R.id.message);
        no_surveys = (TextView) findViewById(R.id.no_surveys);
        no_surveys.setVisibility(View.INVISIBLE);

        mAboutUs = (TextView) findViewById(R.id.about_us);
        new_survey_list = (ListView) findViewById(R.id.new_surveys);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        progressDialog = new ProgressDialog(this);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (isNetworkAvailable(getApplicationContext())) {
                    new RequestRawJSON(USER_SURVEYS_URL+"?facebook_id="+access_token).execute();
                    Log.d(TAG, USER_SURVEYS_URL+"?facebook_id="+access_token);
                } else {
                    Intent no_internet = new Intent(getApplicationContext(), NoInternet.class);
                    no_internet.putExtra("activity", "pro.ambrose.www.tal_surveys.Home");
                    startActivity(no_internet);
                }
            }
        });

        if (isNetworkAvailable(this)) {
            new RequestRawJSON(USER_SURVEYS_URL+"?facebook_id="+this.access_token).execute();
            Log.d(TAG, USER_SURVEYS_URL+"?facebook_id="+access_token);
        } else {
            Intent no_internet = new Intent(getApplicationContext(), NoInternet.class);
            no_internet.putExtra("activity", "pro.ambrose.www.tal_surveys.Home");
            startActivity(no_internet);
        }


        new_survey_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar.make(view, "Press start to begin the survey", Snackbar.LENGTH_LONG).setAction("Start", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent answer_intent = new Intent(getApplicationContext(), AnswerSurvey.class);
                        answer_intent.putExtra("survey_id", new_suvey_data.get(position).getSurveyId());
                        startActivity(answer_intent);
                    }
                }).show();
            }
        });

        mAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, About.class));
            }
        });
    }

    public void updateUI(String rawJSON) throws JSONException {
        new_suvey_data = new ArrayList<>();

        if (rawJSON == null){
            Intent no_internet = new Intent(getApplicationContext(), NoInternet.class);
            no_internet.putExtra("activity", "pro.ambrose.www.tal_surveys.Home");
            startActivity(no_internet);
            finish();
        }

        Log.d(TAG, rawJSON);

        if (rawJSON != null) {
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
            progressDialog.setMessage("Loading surveys ...");
            progressDialog.show();
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
                Log.d(TAG, s);
                Log.d(TAG, access_token);
                updateUI(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.hide();
        }
    }

}
