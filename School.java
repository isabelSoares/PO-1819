package sth;

//FIXME import other classes if needed

import sth.exceptions.BadEntryException;
//import sth.exceptions.InvalidCourseSelectionException;
import sth.exceptions.NoSuchPersonIdException;
import java.util.ArrayList;
import java.io.Serializable;
import java.io.IOException;

/**
 * School implementation.
 */
public class School implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201810051538L;

  //FIXME define object fields (attributes and, possibly, associations)
  private ArrayList<Course> _course;
  private Map<Integer,Person> _persons;
  private ArrayList<Student> _student;
  private int _UserId;

  

  //FIXME implement constructors if needed
  
  /**
   * @param filename
   * @throws BadEntryException
   * @throws IOException
   */
  void importFile(String filename) throws IOException, BadEntryException {
    //FIXME implement text file reader
  }
  
  //FIXME implement other methods
  public void login(int id) throws NoSuchPersonIdException {
    //FIXME implement method
    if (_persons.containsKey(id)){
      _UserId = id;
    }
    else{
      throw new NoSuchPersonIdException(id);
    }

  }
}
