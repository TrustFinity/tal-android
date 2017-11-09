package DataModels;

/**
 * Created by ambrose on 04/11/17.
 */

public class QuestionAnswerModel {
    private int surveyId;
    private String questionType;
    private String question;
    private String[] responses;

    public QuestionAnswerModel(int surveyId, String questionType, String question, String[] responses) {
        this.surveyId = surveyId;
        this.questionType = questionType;
        this.question = question;
        this.responses = responses;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getResponses() {
        return responses;
    }

    public void setResponses(String[] responses) {
        this.responses = responses;
    }
}
