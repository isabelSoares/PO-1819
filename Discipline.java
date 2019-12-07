package sth;
import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class Discipline implements Serializable, Comparable<Discipline>{
    private static final long serialVersionUID = 201720051538L;
    private String _name;
    private TreeMap<Integer,Person> _people;
    private TreeMap<String,Project> _projects;
    private Course _course;
 
    
    public Discipline(String name){
        _name = name; 
        _people = new TreeMap<Integer,Person>();
        _projects= new TreeMap<String, Project>();
    }

    public TreeMap<String,Project> getProjectMap(){
        return _projects;
    }

    public TreeMap<Integer,Person> getPersonMap(){
        return _people;
    }

    public void addPerson(Person p) {
        _people.put(p.getID(),p);
    }

    public boolean hasPersonID(int id){
        return _people.containsKey(id);
    }
    
    public boolean hasPerson(Person p){
        return _people.containsValue(p);
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
  
    public Course getCourse(){
        return _course;
    }

    public String getCourseName(){
        return _course.getName();
    }

    public void setCourse(Course c){
        _course = c;
    }

    public Project getProject(String projectName){
        return _projects.get(projectName);
    }

    public boolean hasProject(Project p){
        return _projects.containsValue(p);
    }
    public boolean hasProjectName(String name){
        return _projects.containsKey(name);
    }

    public void addProject(String name, Project proj){
        _projects.put(name,proj);
    }

    @Override
    public int compareTo(Discipline discipline) {
        Collator col = Collator.getInstance(Locale.getDefault());
        String a = this.getName();
        String b = discipline.getName();
        String c = this.getCourseName();
        String d = discipline.getCourseName();
        int i = col.compare(c, d);
        if (i != 0) return i;
        return col.compare(a, b);
    }

    @Override
    public String toString(){
        return "* " + this.getCourseName() + " - " + _name;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Discipline){
            Discipline d = (Discipline) o;
            return (this.getName().equals(d.getName()) && this.getCourseName().equals(d.getCourseName()));
        }
        return false;
    }
}