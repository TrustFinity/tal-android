package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import DataModels.ModelAdapters.QuestionAnswerModelAdapter;
import DataModels.NewSurveyModel;
import DataModels.QuestionAnswerModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnswerSurvey extends AppCompatActivity implements QuestionAnswerModelAdapter.SendAnswerListener {

    public final String USER_SURVEYS_URL = "http://tal-surveys.herokuapp.com/api/v1/get-survey-questions/";
    ArrayList<NewSurveyModel> new_survey_data;
    int survey_id;
    ListView question_answers;
    ArrayList<QuestionAnswerModel> questionAnswerModels;
    QuestionAnswerModelAdapter questionAnswerModelAdapter;
    TextView success_message;
    Button goback_btn;
    private String TAG = "AnswerSurvey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_survey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new_survey_data = (ArrayList<NewSurveyModel>) getIntent().getSerializableExtra("data");
        survey_id = getIntent().getIntExtra("survey_id", 0);

        question_answers = findViewById(R.id.questions_answers);
        success_message = findViewById(R.id.success_message);
        success_message.setVisibility(View.INVISIBLE);
        goback_btn = findViewById(R.id.go_back_btn);
        goback_btn.setVisibility(View.INVISIBLE);
        Log.d(TAG, "Survey-ID: " + survey_id);
        new RequestRawJSON(USER_SURVEYS_URL + survey_id).execute();
    }

    @Override
    public void onSendAnswerButtonListener(View view, int position, String answer) {
        answer = questionAnswerModels.get(position).getSelectedResponse();
        // send the response to the server.
        // Do all this on successful submission of the response.
        questionAnswerModels.remove(position);
        questionAnswerModelAdapter = new QuestionAnswerModelAdapter(this, questionAnswerModels);
        questionAnswerModelAdapter.setSendAnswerButtonListener(this);
        question_answers.setAdapter(questionAnswerModelAdapter);
        questionAnswerModelAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Answer: " + answer, Toast.LENGTH_SHORT).show();
        if (questionAnswerModels.size() == 0) {
            success_message.setVisibility(View.VISIBLE);
            goback_btn.setVisibility(View.VISIBLE);
            goback_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), NewSurveys.class));
                }
            });
        }
    }

    private void updateUI(String s) {
        Log.d(TAG, s);
        JSONArray questions = null;
        try {
            questions = new JSONArray(s);

            if (questions.length() < 0) {
                success_message.setVisibility(View.VISIBLE);
                success_message.setText("No Question for this survey. Sorry, report this.");
                goback_btn.setVisibility(View.VISIBLE);
                goback_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), NewSurveys.class));
                    }
                });
            }

            questionAnswerModels = new ArrayList<>();
            QuestionAnswerModel questionAnswerModel;
            for (int j = 0; j < questions.length(); j++) {
                questionAnswerModel = new QuestionAnswerModel();
                questionAnswerModel.setSurveyId(Integer.parseInt(questions.getJSONObject(j).getString("survey_id")));
                questionAnswerModel.setQuestionId(Integer.parseInt(questions.getJSONObject(j).getString("id")));
                questionAnswerModel.setQuestionType(questions.getJSONObject(j).getString("type"));
                questionAnswerModel.setQuestion(questions.getJSONObject(j).getString("question"));
                JSONArray responses = questions.getJSONObject(j).getJSONArray("responses");
                String[] string_response = new String[responses.length()];
                for (int k = 0; k < responses.length(); k++) {
                    string_response[k] = responses.getJSONObject(k).getString("answer");
                }
                questionAnswerModel.setResponses(string_response);
                questionAnswerModels.add(questionAnswerModel);
            }
        } catch (JSONException e) {
            // Null pointer no data from the network.
            e.printStackTrace();
        }
        questionAnswerModelAdapter = new QuestionAnswerModelAdapter(getApplicationContext(), questionAnswerModels);
        questionAnswerModelAdapter.setSendAnswerButtonListener(this);
        question_answers.setAdapter(questionAnswerModelAdapter);
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
            updateUI(s);
        }
    }

}
