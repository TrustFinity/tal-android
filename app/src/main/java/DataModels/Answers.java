package DataModels;

/**
 * Created by ambrose on 15/11/2017.
 */

public class Answers {
    private int questionID;
    private int surveyID;
    private int facebookID;
    private String answer;

    public Answers(int questionID, int surveyID, String answer) {
        this.questionID = questionID;
        this.surveyID = surveyID;
        this.answer = answer;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public int getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(int facebookID) {
        this.facebookID = facebookID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
