package DataModels.ModelAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import DataModels.NewSurveyModel;
import DataModels.QuestionAnswerModel;
import pro.ambrose.www.tal_surveys.R;

/**
 * Created by ambrose on 04/11/17.
 */

public class QuestionAnswerModelAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<QuestionAnswerModel> questionAnswerModels;
    private RadioGroup radioGroup;
    private Button sendAnswer;
    String response;


    public QuestionAnswerModelAdapter(Context context, ArrayList<QuestionAnswerModel> questionAnswerModels) {
        this.context = context;
        this.questionAnswerModels = questionAnswerModels;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final QuestionAnswerModel questionAnswerModel = questionAnswerModels.get(position);

        View rowView = LayoutInflater.from(context).inflate(R.layout.question_answer_row, null, true);

        radioGroup = (RadioGroup) rowView.findViewById(R.id.myRadioGroup);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < questionAnswerModel.getResponses().length; i++){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(questionAnswerModel.getResponses()[i]);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                response = questionAnswerModel.getResponses()[checkedId];
            }
        });

        TextView question = (TextView) rowView.findViewById(R.id.question);
        question.setText(questionAnswerModel.getQuestion());

        sendAnswer = (Button) rowView.findViewById(R.id.send_answer);

        sendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
