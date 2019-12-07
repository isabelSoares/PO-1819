package sth;

public class UserDescription extends User{
    @Override
    public String visitStudent(Student s){
        return s.toString();
    }

    @Override
    public String visitProfessor(Professor p){
        return p.toString();
    }

    @Override
    public String visitAdministrative(Administrative a) {
        return a.toString();
    }
}