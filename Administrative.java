package sth;

import java.io.Serializable;

public class Administrative extends Person implements Serializable{
    private static final long serialVersionUID = 201010051538L;
    public Administrative(int id, int phoneNumber, String name){
        super(id,phoneNumber,name);
    }

    @Override
    public String accept(User userDescription) {
        return userDescription.visitAdministrative(this);
    }

    @Override
    public String toString(){
        return "FUNCIONÁRIO" + super.toString();
    }
}