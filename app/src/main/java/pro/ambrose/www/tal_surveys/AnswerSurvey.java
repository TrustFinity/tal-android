package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import DataModels.ModelAdapters.QuestionAnswerModelAdapter;
import DataModels.NewSurveyModel;
import DataModels.QuestionAnswerModel;

public class AnswerSurvey extends AppCompatActivity implements QuestionAnswerModelAdapter.SendAnswerListener {

    ArrayList<NewSurveyModel> new_survey_data;
    int position;
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
        position = getIntent().getIntExtra("position", 0);

        question_answers = (ListView) findViewById(R.id.questions_answers);
        success_message = findViewById(R.id.success_message);
        success_message.setVisibility(View.INVISIBLE);
        goback_btn = findViewById(R.id.go_back_btn);
        goback_btn.setVisibility(View.INVISIBLE);

        question_answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
            }
        });

        String[] dummys = {"Mwaka", "Ambrose", "Okello", "Ruth"};
        questionAnswerModels = new ArrayList<>();
        questionAnswerModels.add(new QuestionAnswerModel(1, 1, "objective", "What is the name of your favourite pet?", dummys));
        questionAnswerModels.add(new QuestionAnswerModel(2, 3, "objective", "What is the name of your grandma?", dummys));
        questionAnswerModels.add(new QuestionAnswerModel(1, 2, "objective", "Now let’s have a look at the code above. When a checked radio button is changed in its group, OnCheckedChangeListener is invoked in order to handle this situa?", dummys));
        questionAnswerModelAdapter = new QuestionAnswerModelAdapter(this, questionAnswerModels);
        questionAnswerModelAdapter.setSendAnswerButtonListener(this);
        question_answers.setAdapter(questionAnswerModelAdapter);

        Log.d(TAG, new_survey_data.get(position).getName());
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
}
