package sth;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Project implements Serializable{
    private static final long serialVersionUID = 207676721138L;
    private String _name;
    private String _description;
    private TreeMap<Integer,String> _submissions;
    private Survey _survey;
    private boolean _openStatus;
    private Discipline _discipline;

    public Project(String name, String description){
        _name = name;
        _description = "";
        _submissions = new TreeMap<Integer,String>();
        _openStatus = true;
    }

    public String getName() {return _name;}

    public Survey getSurvey() {return _survey;}

    public boolean hasSurvey() {return _survey != null;}

    public String getDescription() {return _description;}

    public void addSubmission(Integer id, String submission){
        _submissions.put(id,submission);
    }

    public boolean hasSubmission(Integer id){
        return _submissions.containsKey(id);
    }

    public void setName(String name) {_name = name;}

    public void setDescription(String description) {_description = description;}

    public void setDiscipline(Discipline d) {_discipline = d;}

    public void setSurvey(Survey s) {_survey = s;}

    public void removeSurvey() {_survey = null;}

    public Discipline getDiscipline() {return _discipline;}

    public String getDisciplineName() {return _discipline.getName();}

    public boolean isOpen() {return _openStatus;}

    public void close() {_openStatus = false;}



    @Override
    public String toString(){
        String projectName = new String();
        for(Map.Entry<Integer,String> entry : _submissions.entrySet()){
            projectName = projectName + "* " + entry.getKey().toString() + " - " + entry.getValue() + "\n";
        }
        return projectName;
    }
    
}