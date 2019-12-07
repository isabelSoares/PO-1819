package sth;

import java.io.Serializable;

import sth.exceptions.FinishingSurveyIdException;
import sth.exceptions.NonEmptySurveyIdException;
import sth.exceptions.OpeningSurveyIdException;

public class OpenState extends SurveyState implements Serializable{
    private static final long serialVersionUID = 252521161538L;
    
    
    public OpenState(Survey survey){
        super(survey);
        setStatus("OPENED");
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        survey.notify("Pode preencher inquérito do projecto " + projectName + " da disciplina " + disciplineName);
    }

    public void open() throws OpeningSurveyIdException{
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new OpeningSurveyIdException(disciplineName,projectName);
    }

    public void close() {
        getSurvey().setState(new CloseState(getSurvey()));
    }

    public void cancel() throws NonEmptySurveyIdException {
        String disciplinename = super.getSurvey().getProject().getDisciplineName();
        String projectname = super.getSurvey().getProjectName();
        throw new NonEmptySurveyIdException(disciplinename,projectname);
    }

    public void finalize()throws FinishingSurveyIdException{
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new FinishingSurveyIdException(disciplineName,projectName);
    }
}