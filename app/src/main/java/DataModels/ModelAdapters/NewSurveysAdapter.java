package DataModels.ModelAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import DataModels.NewSurveyModel;
import pro.ambrose.www.tal_surveys.R;

/**
 * Created by ambrose on 14/10/17.
 */

public class NewSurveysAdapter extends BaseAdapter {

    private ArrayList<NewSurveyModel> data;
    private Context context;

    public NewSurveysAdapter(ArrayList<NewSurveyModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
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
        NewSurveyModel survey = data.get(position);
        View rowView = LayoutInflater.from(context).inflate(R.layout.new_survey_layout, null, true);
        TextView name =(TextView) rowView.findViewById(R.id.survey_name);
        TextView description =(TextView) rowView.findViewById(R.id.survey_description);
        name.setText(survey.getName());
        description.setText(survey.getDescription());
        return rowView;
    }
}
