package sth;

import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.FinishingSurveyIdException;
import sth.exceptions.NonEmptySurveyIdException;
import sth.exceptions.OpeningSurveyIdException;
import sth.exceptions.SurveyFinishedIdException;

import java.io.Serializable;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class Survey implements Serializable, Subject{
    private static final long serialVersionUID = 201817563138L;
    private SurveyState _state;
    private Project _project;
    private HashSet<Integer> _ids;
    private ArrayList<Integer> _nhours;
    private ArrayList<String> _comments;
    private ArrayList<Person> _observers;

    public Survey() {
        _state = new CreatedState(this);
        _ids = new HashSet<Integer>();
        _nhours = new ArrayList<Integer>();
        _comments = new ArrayList<String>();
        _observers = new ArrayList<Person>();
    }

    public void setProject(Project p) {_project = p;}

    public Project getProject() {return _project;}

    public String getProjectName() {return _project.getName();}

    public Integer numAnswers(){return _ids.size();}

    public Integer averageHours(){
        Integer total = 0;
        for (Integer i : _nhours){
            total += i;
        }
        Integer average = total / _nhours.size();
        if (average == 0)
            return 0;
        return average;
    }

    public Integer minHours(){
        return Collections.min(_nhours,null);
    }

    public Integer maxHours(){
        return Collections.max(_nhours,null);
    }

    public void setState(SurveyState state) {
        _state = state; 
    }

    public SurveyState getState(){ return _state;}

    public boolean addID(Integer id){
        return _ids.add(id);
    }

    public void addNhours(Integer nhours){
        _nhours.add(nhours);
    }

    public void addComment(String comment){
        _comments.add(comment);
    }

    public void cancel() throws SurveyFinishedIdException, NonEmptySurveyIdException{
        _state.cancel();
    }

    public void open() throws OpeningSurveyIdException{
        _state.open();
    }

    public void close() throws ClosingSurveyIdException{
        _state.close();
    }

    public void finalize() throws FinishingSurveyIdException{
        _state.finalize();
    }

    public void attach(Person o){
        _observers.add(o);
    }
    
    public void detach(Person o){
        int i = _observers.indexOf(o);
        if (i >= 0) { _observers.remove(i); }
    }

    public void notify(String message){
        for (Person o : _observers){
            o.update(message);
        }
    }

}