package sth;
import java.io.Serializable;

import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.OpeningSurveyIdException;

public class CloseState extends SurveyState implements Serializable{
    private static final long serialVersionUID = 975321161538L;
    public CloseState(Survey survey){
        super(survey);
        setStatus("CLOSED");
    }

    public void open() throws OpeningSurveyIdException{
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new OpeningSurveyIdException(disciplineName,projectName);
    }

    public void close() throws ClosingSurveyIdException {
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new ClosingSurveyIdException(disciplineName,projectName);
    }
 

    public void cancel() {
        getSurvey().setState(new OpenState(super.getSurvey()));
    }

    public void finalize(){
        getSurvey().setState(new FinalizeState(super.getSurvey()));
    }

 
}