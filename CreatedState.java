package sth;

import java.io.Serializable;

import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.FinishingSurveyIdException;

public class CreatedState extends SurveyState implements Serializable{
    private static final long serialVersionUID = 975300741538L;
    public CreatedState(Survey survey){
        super(survey);
        setStatus("CREATED");
    }


    public void open() {
        getSurvey().setState(new OpenState(super.getSurvey()));
    }
   
    public void close() throws ClosingSurveyIdException{
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new ClosingSurveyIdException(disciplineName,projectName);
    }
   
    public void cancel() {}

    public void finalize() throws FinishingSurveyIdException{
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new FinishingSurveyIdException(disciplineName,projectName);
    }
    
}
