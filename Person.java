package sth;

import java.util.Comparator;
import java.io.Serializable;
import java.util.ArrayList;
import sth.School;
import sth.exceptions.NoSuchPersonIdException;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.ArrayList;



public abstract class Person implements Serializable,Observer{
    private static final long serialVersionUID = 201710051538L;
    private int _phoneNumber;
    private int _id;
    private String[] _name;
    private ArrayList<String> _notifications;
    
    public Person (int id, int phoneNumber, String name){
        _id = id;
        _phoneNumber = phoneNumber;
        _name = name.split(" ");
        _notifications = new ArrayList<String>();
    }
   
    public int getID() {
        return _id;
    }

    public ArrayList<String> getNotifications(){
        return _notifications;
    }

    public void removeNotifications(){
        _notifications.clear();
    }

    public void setID(int id) {
        _id = id;
    }

    public int getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        _phoneNumber = phoneNumber;
    }

    public String getName(){
        String fullName = "";
        for (String n : _name){
            fullName += n;
            fullName += ' ';
        }
        return fullName.substring(0,fullName.length()-1);
    }

    public void setName(String name) {
        String[] nameArray = name.split(" ");
        _name = nameArray;
    }
 
    public String accept(User userDescription) {
        throw new UnsupportedOperationException();
    }

    public void update(String message){
        _notifications.add(message);
    }

    public static class DisciplineComparator implements Serializable, Comparator<Discipline> {
        private static final long serialVersionUID = 201110051538L;
        @Override
        public int compare(Discipline a, Discipline b){
            Collator col = Collator.getInstance(Locale.getDefault());
            String aStr = a.getName();
            String bStr = b.getName();
            String cStr = a.getCourseName();
            String dStr = b.getCourseName();
            int i = col.compare(cStr, dStr);
            if (i != 0) return i;
            return col.compare(aStr, bStr);
        }
    }

    @Override
    public String toString(){
        String idString = Integer.toString(_id);
        String phoneString = Integer.toString(_phoneNumber);
        String fullName = "";
        for (String n : _name){
            fullName += n;
            fullName += ' ';
        }
        String noSpace = fullName.substring(0,fullName.length()-1);
        return "|" + idString + "|" + phoneString + "|" + noSpace;
    }

}

