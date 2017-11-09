package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import DataModels.ModelAdapters.NewSurveysAdapter;
import DataModels.NewSurveyModel;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewSurveys extends AppCompatActivity {

    public final String USER_SURVEYS_URL = "http://tal-surveys.herokuapp.com/api/v1/get-for-user";
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    ListView new_survey_list;
    private ArrayList<NewSurveyModel> new_suvey_data;
    private String TAG = "NewSurvey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_surveys);
        new_survey_list = (ListView) findViewById(R.id.new_surveys);
        FacebookSdk.sdkInitialize(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new RequestRawJSON(USER_SURVEYS_URL).execute();
            }
        });

        new RequestRawJSON(USER_SURVEYS_URL).execute();

        new_survey_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar.make(view, "Press start to begin the survey", Snackbar.LENGTH_INDEFINITE).setAction("Start", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent answer_intent = new Intent(getApplicationContext(), AnswerSurvey.class);
                        answer_intent.putExtra("position", position);
                        answer_intent.putExtra("data", new_suvey_data);
                        startActivity(answer_intent);
                    }
                }).show();
            }
        });

    }

    public class RequestRawJSON extends AsyncTask<String, String, String> {

        private String URL;
        public RequestRawJSON(String URL) {
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        OkHttpClient client = new OkHttpClient();
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
}
