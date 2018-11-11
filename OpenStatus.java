package sth;

import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.Serializable;

public class openStatus implements Serializable{
    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
        new FileInputStream("actualStatus.dat")));
    //File f = ois.readFile(); n permite isto
    ois.close();
    if (file !=null){// se o ficheiro exitir 
        openFile();
    }
    else{
        System.out.println("fileNotFound()");
    }
}
