package pro.ambrose.www.tal_surveys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import DataModels.Answers;
import DataModels.ModelAdapters.QuestionAnswerModelAdapter;
import DataModels.NewSurveyModel;
import DataModels.QuestionAnswerModel;

public class AnswerSurvey extends AppCompatActivity {

    ArrayList<NewSurveyModel> new_survey_data;
    int position;
    ListView question_answers;
    Button send_button;
    ArrayList<QuestionAnswerModel> questionAnswerModels;
    ArrayList<Answers> questionAnswers;
    QuestionAnswerModelAdapter questionAnswerModelAdapter;
    private String TAG = "AnswerSurvey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_survey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new_survey_data = (ArrayList<NewSurveyModel>) getIntent().getSerializableExtra("data");
        position = getIntent().getIntExtra("position", 0);
        question_answers = (ListView) findViewById(R.id.questions_answers);

        String[] dummys = {"Mwaka", "Ambrose", "Okello", "Ruth"};
        questionAnswerModels = new ArrayList<>();
        questionAnswers = new ArrayList<>();
        questionAnswerModels.add(new QuestionAnswerModel(1, 1, "objective", "What is the name of your favourite pet?", dummys));
        questionAnswerModels.add(new QuestionAnswerModel(1, 2, "objective", "Now let’s have a look at the code above. When a checked radio button is changed in its group, OnCheckedChangeListener is invoked in order to handle this situa?", dummys));
        questionAnswerModelAdapter = new QuestionAnswerModelAdapter(this, questionAnswerModels);
        question_answers.setAdapter(questionAnswerModelAdapter);


        send_button = findViewById(R.id.submit_response);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionAnswers = questionAnswerModelAdapter.getAnswers();
                HashSet hs = new HashSet();
                hs.addAll(questionAnswers);
                questionAnswers.clear();
                questionAnswers.addAll(hs);

                Log.d("Button Click", questionAnswers.size() + "");
                Toast.makeText(AnswerSurvey.this, questionAnswers.get(0).getAnswer(), Toast.LENGTH_SHORT).show();
                for (Answers answers : questionAnswers) {
                    Log.d(TAG, "QuestionID: " + answers.getQuestionID() + " Answer: " + answers.getAnswer());
                }
            }
        });

        Log.d(TAG, new_survey_data.get(position).getName());
    }
}
