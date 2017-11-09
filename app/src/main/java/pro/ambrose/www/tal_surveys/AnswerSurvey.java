package pro.ambrose.www.tal_surveys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import DataModels.ModelAdapters.QuestionAnswerModelAdapter;
import DataModels.NewSurveyModel;
import DataModels.QuestionAnswerModel;

public class AnswerSurvey extends AppCompatActivity {

    ArrayList<NewSurveyModel> new_survey_data;
    int position;
    private String TAG = "AnswerSurvey";
    public String name, description;

    TextView name_view, description_view;
    ListView question_answers;
    ArrayList<QuestionAnswerModel> questionAnswerModels;
    QuestionAnswerModelAdapter questionAnswerModelAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_survey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new_survey_data = (ArrayList<NewSurveyModel>) getIntent().getSerializableExtra("data");
        position = getIntent().getIntExtra("position", 0);
        name_view = (TextView) findViewById(R.id.name);
        description_view = (TextView) findViewById(R.id.description);
        question_answers = (ListView) findViewById(R.id.questions_answers);


        name = new_survey_data.get(position).getName();
        description = new_survey_data.get(position).getDescription();

        name_view.setText(name);
        description_view.setText(description);
        String[] dummys = {"Mwaka", "Ambrose", "Okello", "Ruth"};
        questionAnswerModels = new ArrayList<>();
        questionAnswerModels.add(new QuestionAnswerModel(1, "objective", "What is the name of your favourite pet?", dummys));
        questionAnswerModels.add(new QuestionAnswerModel(1, "objective", "Now let’s have a look at the code above. When a checked radio button is changed in its group, OnCheckedChangeListener is invoked in order to handle this situa?", dummys));

        question_answers.setAdapter(new QuestionAnswerModelAdapter(this, questionAnswerModels));

        Log.d(TAG, new_survey_data.get(position).getName());
    }
}
