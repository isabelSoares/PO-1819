package sth;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Comparator;

public class Professor extends Person implements Serializable{
    private static final long serialVersionUID = 201811161538L;

    private TreeSet<Discipline> _disciplines;
    private TreeMap<String,Course> _courses;

    public Professor(int id, int phoneNumber, String name){ 
        super(id, phoneNumber, name);
        _disciplines = new TreeSet<Discipline>(new DisciplineComparator());
        _courses= new TreeMap<String, Course>();
    }

    public boolean addDiscipline(Discipline d) {
        return _disciplines.add(d);
    }
    public boolean hasDiscipline(Discipline d){
        return _disciplines.contains(d);
    }

    public boolean hasDisciplineName(String name){
        for (Discipline d : _disciplines)
            if (d.getName().equals(name))
                return true;
        return false;
    }

    public Discipline getDisciplineName(String name){
        for (Discipline d : _disciplines)
            if (d.getName().equals(name))
                return d;
        return null;
    }

    public void setCourse(Course c){
        _courses.put(c.getName(),c);
    }

    @Override
    public String accept(User userDescription) {
        return userDescription.visitProfessor(this);
    } 


    @Override
    public String toString(){
        String profName = new String();
        profName = "DOCENTE" + super.toString();
        for (Discipline d : _disciplines)
            profName = profName + "\n" + d.toString();
        return profName;
    }
}