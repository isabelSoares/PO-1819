package sth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap; 

public class Course implements Serializable{
    private static final long serialVersionUID = 201711751538L;
    private String _name;
    private int _repcounter;
    private TreeMap<String, Discipline> _disciplines;
    private TreeMap<Integer,Student> _representatives;

    public Course(String name){
        _name = name;
        _disciplines = new TreeMap<String, Discipline>();
        _representatives = new TreeMap<Integer, Student>();
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getRepCounter() {
        return _repcounter;
    }

    public void setRepCounter(int r) {
        _repcounter = r;
    }
    
    public void addDiscipline(Discipline d){
        _disciplines.put(d.getName(),d);
    }

    public boolean hasDiscipline(Discipline discipline){
        return _disciplines.containsValue(discipline); 
    }

    public boolean hasDisciplineName(String disciplineName){
        return _disciplines.containsKey(disciplineName); 
    }

    public Discipline getDisciplineName(String disciplineName){
        return _disciplines.get(disciplineName);
    }

    public void addRepresentative(Student s){
        _representatives.put(s.getID(),s);
    }
    public boolean hasRepresentative(int id){
        return _representatives.containsKey(id);
    }

    public TreeMap<Integer,Student> getRepresentativeMap(){
        return _representatives;
    }

    @Override
    public String toString(){
        String coursename = new String();
        for (Map.Entry<String,Discipline> entry : _disciplines.entrySet())
            coursename = coursename + "\n" + entry.getValue().toString();
        return coursename;
    }
    @Override
    public boolean equals(Object o){
        if (o instanceof Course){
            Course c = (Course) o;
            this.getName().equals(c.getName());
        }
        return false;
    }
}

