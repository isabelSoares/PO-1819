package sth;

import java.io.Serializable;
import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.FinishingSurveyIdException;
import sth.exceptions.NonEmptySurveyIdException;
import sth.exceptions.OpeningSurveyIdException;
import sth.exceptions.SurveyFinishedIdException;

public  abstract class SurveyState implements Serializable {
    private static final long serialVersionUID = 234321165678L;
    private Survey _survey;
    private String _status;

    public SurveyState(Survey survey){
        _survey = survey;
        _status = new String();
    }

    public Survey getSurvey(){return _survey;}

    public void setStatus(String status){_status = status;}

    public String getStatus() {return _status;}

    public abstract void cancel() throws SurveyFinishedIdException, NonEmptySurveyIdException;

    public abstract void open() throws OpeningSurveyIdException;

    public abstract void close() throws ClosingSurveyIdException;

    public abstract void finalize() throws FinishingSurveyIdException;

}
