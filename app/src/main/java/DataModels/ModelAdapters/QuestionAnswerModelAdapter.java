package DataModels.ModelAdapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.function.Predicate;

import DataModels.Answers;
import DataModels.QuestionAnswerModel;
import pro.ambrose.www.tal_surveys.R;

/**
 * Created by ambrose on 04/11/17.
 */

public class QuestionAnswerModelAdapter extends BaseAdapter {
    String response;
    QuestionAnswerModel questionAnswerModel;
    private Context context;
    private ArrayList<QuestionAnswerModel> questionAnswerModels;
    private RadioGroup radioGroup;
    private ArrayList<Answers> answers;

    public QuestionAnswerModelAdapter(Context context, ArrayList<QuestionAnswerModel> questionAnswerModels) {
        this.context = context;
        this.questionAnswerModels = questionAnswerModels;
        answers = new ArrayList<>();
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    @Override
    public int getCount() {
        return questionAnswerModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        questionAnswerModel = questionAnswerModels.get(position);

        final View rowView = LayoutInflater.from(context).inflate(R.layout.question_answer_row, null, true);

        radioGroup = (RadioGroup) rowView.findViewById(R.id.myRadioGroup);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < questionAnswerModel.getResponses().length; i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(questionAnswerModel.getResponses()[i]);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }

        TextView question = (TextView) rowView.findViewById(R.id.question);
        question.setText(questionAnswerModel.getQuestion());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                response = questionAnswerModel.getResponses()[checkedId];
                questionAnswerModel = questionAnswerModels.get(position);
                // TODO Auto-generated method stub
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        answers.add(new Answers(
                                questionAnswerModel.getQuestionId(),
                                questionAnswerModel.getSurveyId(),
                                btn.getText().toString()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            answers.removeIf(new Predicate<Answers>() {
                                @Override
                                public boolean test(Answers answers) {
                                    return answers.getQuestionID() == questionAnswerModel.getQuestionId();
                                }
                            });
                        }

                        Toast.makeText(context, "Checked: " + btn.getText() + " Question: " + questionAnswerModel.getQuestion(), Toast.LENGTH_SHORT).show();
                        Log.d("Adapter", answers.size() + "");
                    }
                }
            }
        });

        return rowView;
    }
}
