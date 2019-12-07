package sth;

import java.io.Serializable;

import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.FinishingSurveyIdException;
import sth.exceptions.OpeningSurveyIdException;
import sth.exceptions.SurveyFinishedIdException;

public class FinalizeState extends SurveyState implements Serializable{
    private static final long serialVersionUID = 975321995998L;
    public FinalizeState(Survey survey){
        super(survey);
        setStatus("FINALIZED");
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        survey.notify("Pode preencher inquérito do projecto " + projectName+ " da disciplina " + disciplineName);
    }

    public void open() throws OpeningSurveyIdException{
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new OpeningSurveyIdException(disciplineName,projectName);
    }

    public void close() {}


    public void cancel() throws SurveyFinishedIdException {
        String disciplineName = super.getSurvey().getProject().getDisciplineName();
        String projectName = super.getSurvey().getProjectName();
        throw new SurveyFinishedIdException(disciplineName,projectName);
    }

    public void finalize(){}

    
}