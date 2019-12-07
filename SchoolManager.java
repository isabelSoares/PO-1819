package sth;

import sth.exceptions.BadEntryException;
import sth.exceptions.ImportFileException;
import sth.exceptions.NoSuchDisciplineIdException;
import sth.exceptions.NoSuchPersonIdException;
import sth.exceptions.NoSuchProjectIdException;
import sth.exceptions.NoSurveyIdException;
import sth.exceptions.OpeningSurveyIdException;
import sth.exceptions.ClosingSurveyIdException;
import sth.exceptions.FinishingSurveyIdException;
import sth.exceptions.DuplicateProjectIdException;
import sth.exceptions.DuplicateSurveyIdException;
import sth.exceptions.NonEmptySurveyIdException;
import sth.exceptions.SurveyFinishedIdException;
import sth.School;
import java.util.Map;
import java.util.TreeMap;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;


/**
 * The façade class.
 */
public class SchoolManager {

 
  private School _school;
  private String _nameFile;
  private int _id;
 

  public SchoolManager(){
    _school = new School();
    
  }
  /**
   * @param datafile
   * @throws ImportFileException
   * @throws InvalidCourseSelectionException
   */
  public void importFile(String datafile) throws ImportFileException {
    try {
      _school.importFile(datafile);
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException(e);
    }
  }

  /**
   * @param id
   * @throws NoSuchPersonIdException
   */
  public void login(int id) throws NoSuchPersonIdException {
    _school.login(id);
    id = _school.getUserID();
    _id = id;
  }
  
  public String changePhoneNumber(int value) {
    return _school.changePhoneNumber(value);
  }

  /**
   * @return true when the currently logged in person is an administrative
   */
  public boolean hasAdministrative(){
    return _school.isAdministrative(_id);
  }

  /**
   * @return true when the currently logged in person is a professor
   */
  public boolean hasProfessor() {
    return _school.isProfessor(_id);
  }

  /**
   * @return true when the currently logged in person is a student
   */
  public boolean hasStudent() {
    
    return _school.isStudent(_id);
  }

  /**
   * @return true when the currently logged in person is a representative
   */
  public boolean hasRepresentative() {
    return _school.isRepresentative(_id);
  }

  public void save () throws FileNotFoundException, IOException {
      ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
        new FileOutputStream(_nameFile)));
      oos.writeObject(_school);
      oos.close(); 
  }

  public boolean hasFile(){ 
    return _nameFile != null;
  }

  public void setNameFile(String nameFile){ _nameFile = nameFile;}

  public void open()throws NoSuchPersonIdException, FileNotFoundException{
    try{
      ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
        new FileInputStream(_nameFile)));
        School newSchool = (School)ois.readObject(); 
        newSchool.login(_id);
        _school = newSchool;
      ois.close();
    }catch(IOException | ClassNotFoundException e ){ e.printStackTrace();}
  }

  public void createProject(String discipline, String project) throws DuplicateProjectIdException,NoSuchDisciplineIdException{
    _school.createProject(discipline, project);
  }

  public void closeProject(String discipline, String project) throws NoSuchProjectIdException,NoSuchDisciplineIdException,OpeningSurveyIdException{
    _school.closeProject(discipline, project);
  }

  public String showPerson(){ 
    return _school.showPerson();
  }

  public String showAllPersons(){
    return _school.showAllPersons();
  }

  public String searchPerson(String name){
    return _school.searchPerson(name);
  }

  public String showDisciplineStudents(String discipline)throws NoSuchDisciplineIdException{
    return _school.showDisciplineStudents(discipline);
  }

  public void deliverProject(String discipline, String project, String submission) throws NoSuchDisciplineIdException, NoSuchProjectIdException{
    _school.deliverProject(discipline, project, submission);
  }

  public String showProjectSubmissions(String discipline, String project)throws NoSuchProjectIdException,NoSuchDisciplineIdException{
    return _school.showProjectSubmissions(discipline, project);
  }

  public void answerSurvey(String discipline, String project, int nhours, String comment) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException{
    _school.answerSurvey(discipline, project, nhours, comment);
  }

  public void cancelSurvey(String discipline, String project) throws NoSuchDisciplineIdException,NoSuchProjectIdException,NoSurveyIdException,NonEmptySurveyIdException, SurveyFinishedIdException{
    _school.cancelSurvey(discipline, project);
  }

  public void createSurvey(String discipline, String project) throws DuplicateSurveyIdException,NoSuchDisciplineIdException,NoSuchProjectIdException{
    _school.createSurvey(discipline, project);
  }

  public void openSurvey(String discipline, String project) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException,OpeningSurveyIdException{
    _school.openSurvey(discipline, project);
  }

  public void closeSurvey(String discipline, String project) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException,ClosingSurveyIdException{
    _school.closeSurvey(discipline, project);
  }

  public void finishSurvey(String discipline, String project) throws NoSuchDisciplineIdException, NoSuchProjectIdException,NoSurveyIdException,FinishingSurveyIdException{
    _school.finishSurvey(discipline, project);
  }

  public String showDisciplineSurveys(String discipline) throws NoSuchDisciplineIdException{
    return _school.showDisciplineSurveys(discipline);
  }

  public String showSurveyResultsStudent(String discipline, String project) throws NoSuchProjectIdException, NoSurveyIdException, NoSuchDisciplineIdException{
    return _school.showSurveyResultsStudent(discipline, project);
  }

  public String showSurveyResultsProfessor(String discipline, String project) throws NoSuchProjectIdException, NoSurveyIdException, NoSuchDisciplineIdException{
    return _school.showSurveyResultsProfessor(discipline, project);
  }

  public String showNotifications(){
    return _school.showNotifications();
  }
  
 
}
 