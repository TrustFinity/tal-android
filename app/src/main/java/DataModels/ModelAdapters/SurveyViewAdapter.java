package DataModels.ModelAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import DataModels.SurveyModel;
import pro.ambrose.www.tal_surveys.R;

/**
 * Created by ambrose on 13/10/17.
 */

public class SurveyViewAdapter extends RecyclerView.Adapter<SurveyViewAdapter.MyViewHolder> {

    private ArrayList<SurveyModel> surveys;

    public SurveyViewAdapter(ArrayList surveys) {
        this.surveys = surveys;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SurveyModel survey  = surveys.get(position);
        holder.name.setText(survey.getName());
        holder.description.setText(survey.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.surveys.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.survey_name);
            description = (TextView) view.findViewById(R.id.survey_description);
        }
    }

}
