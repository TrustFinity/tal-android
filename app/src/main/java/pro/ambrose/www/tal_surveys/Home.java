package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import DataModels.ModelAdapters.SurveyViewAdapter;
import DataModels.SurveyModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";
    public final String ALL_SURVEYS_URL = "http://tal-surveys.herokuapp.com/api/v1/all-surveys";
    public final String USER_SURVEYS_URL = "http://tal-surveys.herokuapp.com/api/v1/all-surveys";
    public boolean isConnected = false;
    ArrayList<SurveyModel> surveys;
    private TextView mTextMessage;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // case R.id.navigation_all:
                //    mTextMessage.setText(R.string.title_all);
                //    return true;

                 case R.id.navigation_surveys:
//                    startActivity(new Intent(getApplicationContext(), NewSurveys.class));
                    mTextMessage.setText(R.string.title_surveys);
                    return true;
                case R.id.navigation_profile:
                    startActivity(new Intent(getApplicationContext(), RespondentProfile.class));
                    mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new RequestRawJSON(ALL_SURVEYS_URL).execute();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.surveys_recycler_views);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.d(TAG, e.getActionIndex()+"Index");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        new RequestRawJSON(ALL_SURVEYS_URL).execute();
    }

    public boolean monitorConnectivity(){
        ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Boolean>() {
                @Override public void accept(Boolean isConnectedToInternet) {
                    Log.d(TAG, "Internet there: " + isConnectedToInternet);
                    isConnected = isConnectedToInternet;
                }
            });
        return isConnected;
    }

    public void updateUI(String rawJSON) throws JSONException {
        // Null pointer exception thrown here in the below line coz of no
        // internet connection.
        surveys = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(rawJSON);
            for (int i = 0; i < array.length(); i++) {

                surveys.add(new SurveyModel(array.getJSONObject(i).getString("name"),
                        array.getJSONObject(i).getString("description")));
                Log.d(TAG, surveys.get(i).getName());
            }
        } catch (NullPointerException ex) {
            Log.e(TAG, ex.getMessage());
            Log.d(TAG, "Seems like you don't have a fast internet now...");
        }
        mAdapter = new SurveyViewAdapter(surveys);
        mRecyclerView.setAdapter(mAdapter);
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
            /*if (monitorConnectivity()){
                return "We are having trouble connecting to the internet. " +
                        "\nAre you sure you have internet access?" +
                        "\n We will keep trying though";
            }*/
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
