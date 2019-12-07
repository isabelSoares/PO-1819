package sth;

public interface Subject{
    public void attach(Person o);

    public void detach(Person o);

    public void notify(String message);
}