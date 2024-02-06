package edu.virginia.cs;

public class Student {
    private int ID;
    private String Login;
    private String Password;
    public Student(String login, String password){
        this.ID = ID;
        this.Login = login;
        this.Password = password;
    }
    public int getID(){
        return ID;
    }
    public String getLogin(){
        return Login;
    }
    public String getPassword(){
        return Password;
    }
}
