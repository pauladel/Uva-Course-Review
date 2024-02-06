package edu.virginia.cs;

public class Course {
    private int ID;
    private String Department;
    private String Catalog;
    public Course (String Department, String catalog){
        this.ID = ID;
        this.Department = Department;
        this.Catalog = catalog;

    }
    public int getID(){
        return ID;
    }
    public String getDepartment(){
        return Department;
    }
    public String getCatalog(){
        return Catalog;
    }
}
