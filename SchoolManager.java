package sth;

import sth.exceptions.BadEntryException;
import sth.exceptions.ImportFileException;
import sth.exceptions.NoSuchPersonIdException;
import sth.app.main;
import sth.School;
import java.util.ArrayList;

import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
//import java.io.IOException;

//FIXME import other classes if needed

/**
 * The façade class.
 */
public class SchoolManager {

  //FIXME add object attributes if needed
  School _school;

  //FIXME implement constructors if needed
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
  }

  /**
   * @return true when the currently logged in person is an administrative
   */
  public boolean hasAdministrative() {
    //FIXME implement predicate
  }

  /**
   * @return true when the currently logged in person is a professor
   */
  public boolean hasProfessor() {
    //FIXME implement predicate
  }

  /**
   * @return true when the currently logged in person is a student
   */
  public boolean hasStudent() {
    //FIXME implement predicate
  }

  /**
   * @return true when the currently logged in person is a representative
   */
  public boolean hasRepresentative() {
    //FIXME implement predicate
  }

  //FIXME implement other methods (in general, one for each command in sth-app)

  public void save () throws ImportFileException {
    // o estado atual é a school 
      ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
      new FileOutputStream("school.dat")));//nome do ficheiro dado
      oos.writeobject(School);
      oos.close(); 
  }
}
