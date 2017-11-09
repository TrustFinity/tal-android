package DataModels;

import java.io.Serializable;

/**
 * Created by ambrose on 14/10/17.
 */

public class NewSurveyModel implements Serializable {

    private int surveyId;
    private int questionId;
    private String name;
    private String description;

    public NewSurveyModel(int surveyId, int questionId, String name, String description, String question) {
        this.surveyId = surveyId;
        this.questionId = questionId;
        this.name = name;
        this.description = description;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
