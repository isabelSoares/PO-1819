package sth;
 
import sth.exceptions.DuplicateProjectIdException;
import sth.exceptions.NoSuchProjectIdException;
import sth.exceptions.NoSuchDisciplineIdException;
import sth.exceptions.BadEntryException;
import sth.exceptions.NoSuchPersonIdException;
import sth.exceptions.NoSurveyIdException;
import sth.exceptions.OpeningSurveyIdException;
import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.FinishingSurveyIdException;
import sth.exceptions.DuplicateSurveyIdException;
import sth.exceptions.NonEmptySurveyIdException;
import sth.exceptions.SurveyFinishedIdException;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.text.Collator;
import java.util.Locale;
import java.util.Collection;
import java.util.Collections;
import java.io.Serializable;
import java.io.IOException;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * School implementation.
 */
public class School implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201810051538L;

  /** The map of courses that the school has */
  private Map<String,Course> _course;

  /** The map of students that the school has */
  private Map<Integer,Student> _students;
  
  /** The map of professors that the school has */
  private Map<Integer,Professor> _professors;

  /** The map of administratives that the school has */
  private Map<Integer,Administrative> _administratives;

  /** The logged user id */
  private int _userId;

  public School(){
    _course = new TreeMap<String,Course>();
    _students = new TreeMap<Integer, Student>();
    _professors = new TreeMap<Integer, Professor>();
    _administratives = new TreeMap<Integer, Administrative>();
  }

  /**
   * @param filename
   *              indicates imported file.
   * @throws BadEntryException
   *              in case of read errors.
   * @throws IOException
   *              in case of read errors.
   */
  public void importFile(String filename) throws IOException, BadEntryException{
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    String line;
              String type = "";
          char cardinal = '#';
          int lastId = 0;
      while ((line = reader.readLine()) != null) {
        String[] fields = line.split("\\|");
        try {
          Pattern patStudent = Pattern.compile("^(ALUNO)");
          Pattern patProfessor = Pattern.compile("^(DOCENTE)");
          Pattern patRepresentative = Pattern.compile("^(DELEGADO)");
          Pattern patAdministrative = Pattern.compile("^(FUNCIONÁRIO)");
          if (patStudent.matcher(fields[0]).matches()) {
            lastId = registerStudent(fields);
            type = "student";
          } else if (patProfessor.matcher(fields[0]).matches()) {
            lastId = registerProfessor(fields);
            type = "professor";
          } else if (patRepresentative.matcher(fields[0]).matches()) {
            lastId = registerStudent(fields); 
            type = "representative";
          } else if (patAdministrative.matcher(fields[0]).matches()) {
            registerAdministrative(fields);
            type = "administrative";
          }else if (fields[0].charAt(0) == cardinal) {

            registerCourse(lastId, type, fields);
          }
        } catch (BadEntryException e) {
          System.err.printf("WARNING: unknown data %s\n", e.getMessage());
          e.printStackTrace();
        }
      }
    reader.close();
  }

  /**
   * @param fields
   *          indicates the String that contains all fields.
   * @throws BadEntryException
   *          in case of read errors.
   * @return last person id.
   */
  public int registerStudent(String... fields) throws BadEntryException {
    int id = Integer.parseInt(fields[1]);
    int phoneNumber = Integer.parseInt(fields[2]);
    if (fields[0].equals("ALUNO") || fields[0].equals("DELEGADO")) {
      Student s = new Student(id,phoneNumber, fields[3]); 
      addStudent(id, s); 
      return id;
    }else {
      throw new BadEntryException(fields[0]);
    }
  }

  /**
   * @param fields
   *          indicates the String that contains all fields.
   * @throws BadEntryException
   *          in case of read errors.
   * @return last professor id.
   */
  public int registerProfessor(String... fields) throws BadEntryException {
    int id = Integer.parseInt(fields[1]);
    int phoneNumber = Integer.parseInt(fields[2]);
    if (fields[0].equals("DOCENTE")) {
      Professor p = new Professor(id, phoneNumber, fields[3]); 
      addProfessor(id, p);
      return id;
    }
    else {
      throw new BadEntryException(fields[0]);
    }
  }

  /**
   * @param fields
   *        indicates the String that contains all fields.
   * @throws BadEntryException
   *        in case of read errors.
   */
  public void registerAdministrative(String... fields) throws BadEntryException {
    int id = Integer.parseInt(fields[1]);
    int phoneNumber = Integer.parseInt(fields[2]);
    if (fields[0].equals("FUNCIONÁRIO")) {
      Administrative a = new Administrative(id, phoneNumber, fields[3]); 
      addAdministrative(id, a);
    }
    else {
      throw new BadEntryException(fields[0]);
    }
  }

  /**
   * @param lastId
   *        indicates the id of the last logged person. 
   * @param type
   *        indicates whether student was a representative.
   * @param fields
   *        indicates the String that contains all fields.
   * @throws BadEntryException
   *        in case of read errors.
   */
  public void registerCourse(int lastId, String type, String... fields) throws BadEntryException {
    String courseName = fields[0].substring(2);
    String discipline = fields[1];
    Course c = new Course(courseName);
    if (_course.containsKey(courseName)){
      Course currentCourse = _course.get(courseName);
      if (currentCourse.hasDisciplineName(discipline)){
        Discipline d = currentCourse.getDisciplineName(discipline);
        currentCourse.addDiscipline(d);
        CourseSetter(type, lastId, currentCourse,d);
      }
      else{
        Discipline d = new Discipline(discipline);
        d.setCourse(c);
        currentCourse.addDiscipline(d);
        CourseSetter(type, lastId, currentCourse,d);
      }
    }else{
      Discipline d = new Discipline(discipline);
      d.setCourse(c);
      c.addDiscipline(d);
      _course.put(c.getName(),c);
      CourseSetter(type, lastId, c,d);
    } 
  }

  /**
   * @param id
   *        indicates the logged id.
   * @throws NoSuchPersonIdException
   *         in case of there is no id's person in school.
   */
  public void login(int id) throws NoSuchPersonIdException {
    if (_students.containsKey(id)){
      _userId = id;
    }else if(_professors.containsKey(id)){
      _userId = id;
    }else if(_administratives.containsKey(id)){
      _userId = id;
    }else{
      throw new NoSuchPersonIdException(id);
    }
  }

  /**
   * @return the user id.
   */
  public int getUserID(){
    return _userId;
  }

  /**
   * @param type
   *        indicates the type of person that is currently adding.
   * @param lastId
   *        indicates the id of the last logged person.
   * @param c
   *        indicates the course that will be added.
   * @param d
   *        indicates the discipline that will be added.
   */
  public void CourseSetter(String type,int lastId, Course c,Discipline d){
    
    if (type.equals("student") || type.equals("representative")){
      Student s = _students.get(lastId);
      if (type.equals("representative"))
        c.addRepresentative(s);
      s.setCourse(c);
      s.addDiscipline(d);
      d.addPerson(s);
    }
    else if (type.equals("professor")){
      Professor p = _professors.get(lastId);
      p.setCourse(c);
      p.addDiscipline(d);
      _professors.put(lastId,p);
      d.addPerson(p);
    }
  }

  /**
   * @param id
   *        indicates the person's id.
   * @param person
   *        indicates person that will be added.
   */
  public void addStudent(int id, Student person){
    _students.put(id,person);
  }

  /**
   *  @param id
   *        indicates the person's id.
   * @param person
   *        indicates person that will be added.
   */
  public void addProfessor(int id, Professor person){
    _professors.put(id,person);
  }

 
  /**
   *  @param id
   *        indicates the person's id.
   * @param person
   *        indicates person that will be added.
   */
  public void addAdministrative(int id, Administrative person){
    _administratives.put(id,person);
  }

  /**
   * @param id
   *         indicates the student's id.
   * @return true whether id indicates a student;
   *        false,otherwise.
   */
  public boolean isStudent(int id){
    return _students.containsKey(id);
  }

  /**
   * @param id
   *         indicates the professor's id.
   * @return true whether id indicates a professor;
   *        false,otherwise.
   */
  public boolean isProfessor(int id){
    return _professors.containsKey(id);
  }

  /**
   * @param id
   *          indicates the administrative's id.
   * @return true whether id indicates a professor;
   *        false,otherwise.
   */
  public boolean isAdministrative(int id){
    return _administratives.containsKey(id);
  }

  /**
   * @param id
   *          indicates the representative's id.
   * @return true whether id indicates a representative;
   *        false,otherwise.
   */
  public boolean isRepresentative(int id){
    Student s = _students.get(id);
    if (s != null)
      return s.isRepresentative(id);
    return false;
  }

 
  /**
   * @param value
   *        indicates the new phoneNumber value.
   * @return
   *        returns the person's information.
   */
  public String changePhoneNumber(int value){
    if (_professors.containsKey(_userId)){
      Professor p = _professors.get(_userId);
      p.setPhoneNumber(value);
      UserDescription userProfessor = new UserDescription();
      return p.accept(userProfessor);
    }
    else if (_students.containsKey(_userId)){
      Student s= _students.get(_userId);
      s.setPhoneNumber(value);
      UserDescription userStudent = new UserDescription();
      return s.accept(userStudent);
    }
    else{
      Administrative a = _administratives.get(_userId);
      a.setPhoneNumber(value);
      UserDescription userAdmnistrative = new UserDescription();
      return a.accept(userAdmnistrative);
    }
  }
  /**
   * @return returns the person information.
   */
  public String showPerson(){
    UserDescription user = new UserDescription();
    if (_professors.containsKey(_userId)){
      Professor p = _professors.get(_userId);
      return p.accept(user);
    }
    else if (_students.containsKey(_userId)){
      Student s= _students.get(_userId);
      return s.accept(user);
    }
    else{
      Administrative a = _administratives.get(_userId);
      return a.accept(user);
    }
  }

  /**
   * @return returns the information of every person.
   */
  public String showAllPersons(){
    TreeMap<Integer, String> _persons = new TreeMap<Integer, String>();
    UserDescription user = new UserDescription();
    for(Map.Entry<Integer,Student> entry : _students.entrySet()) {
      Integer id = entry.getKey();
      String info = (entry.getValue()).accept(user);
      _persons.put(id,info);
    }
    for(Map.Entry<Integer,Professor> entry : _professors.entrySet()) {
      Integer id = entry.getKey();
      String info = (entry.getValue()).accept(user);
      _persons.put(id,info);
    }
    for(Map.Entry<Integer,Administrative> entry : _administratives.entrySet()) {
      Integer id = entry.getKey();
      String info = (entry.getValue()).accept(user);
      _persons.put(id,info);
    }
    String allPeople = "";
    for(Map.Entry<Integer,String> entry : _persons.entrySet()) {
      String info = (entry.getValue());
      allPeople = allPeople + info + "\n";
    }
    return allPeople;
  }

  /**
   * @param name
   *        the name that will be searched.
   * @return
   *        returns the information of every person with that name.
   */
  public String searchPerson(String name){
    CharSequence nameSequence = name;  
    TreeMap<String, Person> _persons = new TreeMap<String, Person>();
    for(Map.Entry<Integer,Student> entry : _students.entrySet()) {
      String stdName = (entry.getValue()).getName();
      Person std = (Person) entry.getValue();
      if (stdName.contains(nameSequence))
        _persons.put(stdName,std);
    }
    for(Map.Entry<Integer,Professor> entry : _professors.entrySet()) {
      String profName = (entry.getValue()).getName();
      Person prof = (Person) entry.getValue();
      if (profName.contains(nameSequence))
        _persons.put(profName,prof);
    }
    for(Map.Entry<Integer,Administrative> entry : _administratives.entrySet()) {
      String admnName = (entry.getValue()).getName();
      Person admn = (Person) entry.getValue();
      if (admnName.contains(nameSequence))
        _persons.put(admnName,admn);
    }
    String allPeople = "";
    UserDescription user = new UserDescription();
    for(Map.Entry<String,Person> entry : _persons.entrySet()) {
      String info = (entry.getValue()).accept(user);
      allPeople = allPeople + info + "\n";
    }
    return allPeople;
  }

  /**
   * @param discipline
   *          indicates the name of a discipline.
   * @return
   *          returns the information of the students who have that discipline.
   * @throws NoSuchDisciplineIdException
   *          in case of an invalid discipline name.
   */
  public String showDisciplineStudents(String disciplineName) throws NoSuchDisciplineIdException{
    Professor p = _professors.get(_userId);
    UserDescription user = new UserDescription();
    if (p.hasDisciplineName(disciplineName)){
      TreeMap<Integer, String> _disStudents = new TreeMap<Integer, String>();
      for(Map.Entry<Integer,Student> entry : _students.entrySet()) {
        Integer id = entry.getKey();
        Student s = entry.getValue();
        String info = (entry.getValue()).accept(user);
        if (s.hasDisciplineName(disciplineName))
          _disStudents.put(id,info);
      }
      String allPeople = "";
      for(Map.Entry<Integer,String> entry : _disStudents.entrySet()) {
        String info = (entry.getValue());
        allPeople = allPeople + info + "\n";
      }
    return allPeople;
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void createProject(String disciplineName, String projectName) throws DuplicateProjectIdException,NoSuchDisciplineIdException{
    Professor p = _professors.get(_userId);
    if (p.hasDisciplineName(disciplineName)){
      Discipline d = p.getDisciplineName(disciplineName);
      if(!(d.hasProjectName(projectName))){
        String description = "";
        Project project = new Project(projectName,description);
        project.setDiscipline(d);
        d.addProject(projectName, project);
      }
      else
        throw new DuplicateProjectIdException(disciplineName,projectName);
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void closeProject(String disciplineName, String projectName) throws NoSuchProjectIdException,NoSuchDisciplineIdException,OpeningSurveyIdException{
    Professor p = _professors.get(_userId);
    if (p.hasDisciplineName(disciplineName)){
        Discipline d = p.getDisciplineName(disciplineName);
        if(d.hasProjectName(projectName)){
          Project currentProject = d.getProject(projectName);
          if (currentProject.isOpen()){
            currentProject.close();
            if (currentProject.hasSurvey()){
              Survey s = currentProject.getSurvey();
              SurveyState state = s.getState();
              if ((state.getStatus()).equals("CREATED"))
                s.open();
              else
                throw new OpeningSurveyIdException(disciplineName, projectName);
            }
          }
        }else
          throw new NoSuchProjectIdException(disciplineName, projectName);
    }else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void deliverProject(String disciplineName, String projectName,String submission) throws NoSuchDisciplineIdException, NoSuchProjectIdException{
    Student s = _students.get(_userId);
    if (s.hasDisciplineName(disciplineName)){
      Discipline d = s.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
        if (currentProject.isOpen())
          currentProject.addSubmission(_userId, submission);
        else
          throw new NoSuchProjectIdException(disciplineName, projectName);
      }
      else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }
    else{
      throw new NoSuchDisciplineIdException(disciplineName);
    }
  }

  public String showProjectSubmissions(String disciplineName, String projectName)throws NoSuchProjectIdException, NoSuchDisciplineIdException{
    Professor p = _professors.get(_userId);
    if (p.hasDisciplineName(disciplineName)){
      Discipline d = p.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
        String projects = disciplineName + " - " + projectName + "\n";
        projects = projects + currentProject.toString();
        return projects;
      }
      else
        throw new NoSuchProjectIdException(disciplineName, projectName);    
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void answerSurvey(String disciplineName, String projectName, int nhours, String comment) throws NoSuchDisciplineIdException,NoSuchProjectIdException,NoSurveyIdException{
    Student s = _students.get(_userId);
    if (s.hasDisciplineName(disciplineName)){
      Discipline d = s.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
          if (currentProject.hasSubmission(_userId)){
            if (currentProject.hasSurvey()){
              Survey currentSurvey = currentProject.getSurvey();
              boolean hasntAnswered = currentSurvey.addID(_userId);
              if (hasntAnswered){
                currentSurvey.addNhours(nhours);
                currentSurvey.addComment(comment);
              }
            }else
              throw new NoSurveyIdException(disciplineName, projectName);
          }else
            throw new NoSuchProjectIdException(disciplineName, projectName);
      }else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }else{
      throw new NoSuchDisciplineIdException(disciplineName);
    }
  }

  public void cancelSurvey(String disciplineName, String projectName) throws NoSuchProjectIdException,NoSuchDisciplineIdException,NoSurveyIdException,NonEmptySurveyIdException, SurveyFinishedIdException{
    Student s = _students.get(_userId);
    Course course = s.getCourse();
    if (course.hasDisciplineName(disciplineName)){
      Discipline d = course.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
          if (currentProject.hasSurvey()){
            Survey currentSurvey = currentProject.getSurvey();
            SurveyState currentState = currentSurvey.getState();
            String surveyStatus = currentState.getStatus();
            if(surveyStatus.equals("CREATED") || (surveyStatus.equals("OPENED") && currentSurvey.numAnswers() == 0))
              currentProject.removeSurvey();
            else{
              currentSurvey.cancel();
            }
          }
        else
          throw new NoSurveyIdException(disciplineName, projectName);
      }
      else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void createSurvey(String disciplineName, String projectName) throws DuplicateSurveyIdException,NoSuchProjectIdException, NoSuchDisciplineIdException{
    Student s = _students.get(_userId);
    Course course = s.getCourse();
    if (course.hasDisciplineName(disciplineName)){
      Discipline d = course.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
        if (currentProject.isOpen()){
          if (currentProject.hasSurvey() == false){
            Survey currentSurvey = new Survey();
            currentSurvey.setProject(currentProject);
            currentProject.setSurvey(currentSurvey);
            TreeMap<Integer,Person> _people = d.getPersonMap();
            //for( Course course: _courses.values())
            for (Map.Entry<Integer,Student> entry : course.getRepresentativeMap().entrySet())
              _people.put(entry.getKey(),entry.getValue());
            for (Map.Entry<Integer,Person> entry : _people.entrySet())
              currentSurvey.attach(entry.getValue());
          }
          else
            throw new DuplicateSurveyIdException(disciplineName, projectName);
        }
        else
          throw new NoSuchProjectIdException(disciplineName, projectName);
      }
      else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void openSurvey(String disciplineName, String projectName) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException,OpeningSurveyIdException{
    Student s = _students.get(_userId);
    Course course = s.getCourse();
    if (course.hasDisciplineName(disciplineName)){
      Discipline d = course.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
          if (currentProject.hasSurvey()){
            Survey currentSurvey = currentProject.getSurvey();
            currentSurvey.open();
          }
        else
          throw new NoSurveyIdException(disciplineName, projectName);
      }
      else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public void closeSurvey(String disciplineName, String projectName) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException,ClosingSurveyIdException{
    Student s = _students.get(_userId);
    Course course = s.getCourse();
    if (course.hasDisciplineName(disciplineName)){
      Discipline d = course.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
          if (currentProject.hasSurvey()){
            Survey currentSurvey = currentProject.getSurvey();
            currentSurvey.close();
          }
        else
          throw new NoSurveyIdException(disciplineName, projectName);
      }
      else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }
    else
      throw new NoSuchDisciplineIdException(disciplineName);
  }
 
  public void finishSurvey(String disciplineName, String projectName) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException,FinishingSurveyIdException{
    Student s = _students.get(_userId);
    Course course = s.getCourse();
    if (course.hasDisciplineName(disciplineName)){
      Discipline d = course.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
          if (currentProject.hasSurvey()){
            Survey currentSurvey = currentProject.getSurvey();
            TreeMap<Integer,Person> _people = d.getPersonMap();
            for (Map.Entry<Integer,Student> entry : course.getRepresentativeMap().entrySet())
              _people.put(entry.getKey(),entry.getValue());
            for (Map.Entry<Integer,Person> entry : _people.entrySet())
              currentSurvey.attach(entry.getValue());
            currentSurvey.finalize();
          }else
            throw new NoSurveyIdException(disciplineName, projectName);
      }else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public String showDisciplineSurveys(String disciplineName) throws NoSuchDisciplineIdException{
    Student s = _students.get(_userId);
    Course course = s.getCourse();
    if (course.hasDisciplineName(disciplineName)){
      Discipline d = course.getDisciplineName(disciplineName);
      TreeMap<String,Survey> _surveysToPrint = new TreeMap<String,Survey>();
      for (Map.Entry<String,Project> entry : d.getProjectMap().entrySet()){
        Project p = entry.getValue();
        if (p.hasSurvey()){
          Survey surv = p.getSurvey();
          _surveysToPrint.put(p.getName(),surv);
        }
      }
      String res = new String();
      for (Map.Entry<String,Survey> entry : _surveysToPrint.entrySet()){
        Survey currentSurvey = entry.getValue();
        SurveyState currentState = currentSurvey.getState();
        String status = currentState.getStatus();
        if (status.equals("CREATED")){
          res = res + disciplineName + " - " + entry.getKey() + " (por abrir)\n";
        }
        else if (status.equals("OPENED")){
          res = res + disciplineName + " - " + entry.getKey() + " (aberto)\n";
        }
        else if (status.equals("CLOSED")){
          res = res + disciplineName + " - " + entry.getKey() + " (fechado)\n";

        }
        else{
          res = res + disciplineName + " - " + entry.getKey() + " " + 
          currentSurvey.numAnswers().toString() + " respostas - " +
          currentSurvey.averageHours().toString() + " horas\n";
        }
      }
      return res;

    }else
      throw new NoSuchDisciplineIdException(disciplineName);
  }
  
  public String showSurveyResultsStudent(String disciplineName, String projectName) throws NoSuchProjectIdException, NoSurveyIdException,NoSuchDisciplineIdException{
    Student s = _students.get(_userId);
    if (s.hasDisciplineName(disciplineName)){
      Discipline d = s.getDisciplineName(disciplineName);
      if(d.hasProjectName(projectName)){
        Project currentProject = d.getProject(projectName);
          if (currentProject.hasSubmission(_userId)){
            if (currentProject.hasSurvey()){
              Survey currentSurvey = currentProject.getSurvey();
              SurveyState currentState = currentSurvey.getState();
              String status = currentState.getStatus();
              String res = surveyResultstoString("student", disciplineName,projectName,status,currentSurvey);
              return res;
            }else
              throw new NoSurveyIdException(disciplineName, projectName);
          }else
            throw new NoSuchProjectIdException(disciplineName, projectName);
      }else
        throw new NoSuchProjectIdException(disciplineName, projectName);
    }else
      throw new NoSuchDisciplineIdException(disciplineName);
  }

  public String showSurveyResultsProfessor(String disciplineName, String projectName) throws NoSuchProjectIdException, NoSurveyIdException,NoSuchDisciplineIdException{
    Professor p = _professors.get(_userId);
    if (p.hasDisciplineName(disciplineName)){
        Discipline d = p.getDisciplineName(disciplineName);
        if(d.hasProjectName(projectName)){
          Project currentProject = d.getProject(projectName);
          if (currentProject.hasSurvey()){
            Survey currentSurvey = currentProject.getSurvey();
            SurveyState currentState = currentSurvey.getState();
            String status = currentState.getStatus();
            String res = surveyResultstoString("professor", disciplineName,projectName,status,currentSurvey);
            return res;
            }
          else
            throw new NoSurveyIdException(disciplineName, projectName);
        }else
          throw new NoSuchProjectIdException(disciplineName, projectName);
    }else
    throw new NoSuchDisciplineIdException(disciplineName);
  }

  public String showNotifications(){
    String res = new String();
    if (isProfessor(_userId)){
      Professor p =  _professors.get(_userId);
      for (String notification : p.getNotifications())
        res = res + notification + "\n";
      p.removeNotifications();
    }
    else if (isStudent(_userId)){
      Student s = _students.get(_userId);
      for (String notification : s.getNotifications())
        res = res + notification + "\n";
      s.removeNotifications();
    }
    
    return res;
  }

  public String surveyResultstoString(String type, String disciplineName, String projectName, String status, Survey currentSurvey){
    String res = new String();
    if (status.equals("CREATED")){
      res = disciplineName + " - " + projectName + " (por abrir)\n";
      return res;
    }
    else if (status.equals("OPENED")){
      res = disciplineName + " - " + projectName + " (aberto)\n";
      return res;
    }
    else if (status.equals("CLOSED")){
      res = disciplineName + " - " + projectName + " (fechado)\n";
      return res;
    }
    else if (type.equals("professor")){
      res = disciplineName + " - " + projectName + "\n" +
      " * Número de respostas: " + currentSurvey.numAnswers().toString() + "\n" +
      " * Tempo médio (horas): " + currentSurvey.minHours().toString() + ", " + 
      currentSurvey.averageHours().toString() + ", " + currentSurvey.maxHours().toString();
      return res;
    }
    else{
      res = disciplineName + " - " + projectName + "\n" +
      " * Número de respostas: " + currentSurvey.numAnswers().toString() + "\n" +
      " * Tempo médio (horas): " + currentSurvey.averageHours().toString() + "\n";
      return res;
    }
  }
}

