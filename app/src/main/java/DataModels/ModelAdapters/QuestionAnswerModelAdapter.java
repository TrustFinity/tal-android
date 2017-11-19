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

import java.util.ArrayList;

import DataModels.Answers;
import DataModels.QuestionAnswerModel;
import pro.ambrose.www.tal_surveys.R;

/**
 * Created by ambrose on 04/11/17.
 */

public class QuestionAnswerModelAdapter extends BaseAdapter {
    SendAnswerListener sendAnswerListener;
    private Context context;
    private ArrayList<Answers> answers;
    private ArrayList<QuestionAnswerModel> questionAnswerModels;
    private RadioButton radioButton;

    public QuestionAnswerModelAdapter(Context context, ArrayList<QuestionAnswerModel> questionAnswerModels) {
        this.context = context;
        this.questionAnswerModels = questionAnswerModels;
        answers = new ArrayList<>();
    }

    public void setSendAnswerButtonListener(SendAnswerListener sendAnswerListener) {
        this.sendAnswerListener = sendAnswerListener;
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    @Override
    public int getCount() {
        return questionAnswerModels.size();
    }

    @Override
    public QuestionAnswerModel getItem(int position) {
        return questionAnswerModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return questionAnswerModels.get(position).getQuestionId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.question_answer_row, null, true);
            viewHolder.radioGroup = convertView.findViewById(R.id.myRadioGroup);
            viewHolder.sendButton = convertView.findViewById(R.id.send_answer);
            viewHolder.radioGroup.setOrientation(LinearLayout.VERTICAL);
            viewHolder.question = convertView.findViewById(R.id.question);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = position;
        viewHolder.questionAnswerModel = questionAnswerModels.get(viewHolder.position);

        for (int i = 0; i < viewHolder.questionAnswerModel.getResponses().length; i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(viewHolder.questionAnswerModel.getResponses()[i]);
            radioButton.setId(i);
            viewHolder.radioGroup.addView(radioButton);
        }

        viewHolder.question.setText(viewHolder.questionAnswerModel.getQuestion());

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = viewHolder.radioGroup.findViewById(checkedId);
                viewHolder.selectedAnswer = radioButton.getText().toString();
                viewHolder.questionAnswerModel.setSelectedResponse(viewHolder.selectedAnswer);
            }
        });

        viewHolder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswerListener.onSendAnswerButtonListener(v, position, viewHolder.questionAnswerModel.getSelectedResponse());
            }
        });

        return convertView;
    }

    public interface SendAnswerListener {
        void onSendAnswerButtonListener(View view, int position, String answer);
    }

    private class ViewHolder {
        TextView question;
        RadioGroup radioGroup;
        Button sendButton;
        int position;
        QuestionAnswerModel questionAnswerModel;
        String selectedAnswer;
    }
}
