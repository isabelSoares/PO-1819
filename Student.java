package sth; 

import java.io.Serializable;
import java.util.TreeSet;
import java.util.Comparator;

public class Student extends Person implements Serializable{
    private static final long serialVersionUID = 201543251538L;
    private Course _scourse;
    private TreeSet<Discipline> _disciplines;


    public Student(int id, int phoneNumber, String name){
        super(id, phoneNumber, name);
        _scourse = new Course("");
        _disciplines = new TreeSet<Discipline>(new DisciplineComparator());
    }

    public Course getCourse(){
        return _scourse;
    } 

    public String getCourseName(){
        return _scourse.getName();
    }

    public void setCourse(Course c){
        _scourse = c;
    }

    public boolean addDiscipline(Discipline d){
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

    public boolean checkDiscipline(String discipline){
        return _scourse.hasDisciplineName(discipline);
    }

    public boolean isRepresentative(int id){
      return _scourse.hasRepresentative(id);
    }

    @Override
    public String accept(User userDescription) {
        return userDescription.visitStudent(this);
    }

    @Override
    public String toString(){
        String studentName = new String();
        if (this.isRepresentative(this.getID()))
            studentName = "DELEGADO" + super.toString();
        else
            studentName = "ALUNO" + super.toString();
        for (Discipline d : _disciplines)
            studentName = studentName + "\n" + d.toString();
        return studentName;
    }
}