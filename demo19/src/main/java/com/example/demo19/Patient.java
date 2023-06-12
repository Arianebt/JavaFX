package com.example.demo19;

public class Patient {
    private int idPatient;
    private String Name;
    private String Sex;
    private int Age;
    private String add_condition;
    private String date_of_birth;

    public Patient(int idPatient, String Name, String Sex, int Age, String add_condition, String Date_Of_Birth) {
        this.idPatient = idPatient;
        this.Name = Name;
        this.Sex = Sex;
        this.Age = Age;
        this.add_condition = add_condition;
        this.date_of_birth = Date_Of_Birth;
    }

    public int getId() {
        return idPatient;
    }

    public String getName() {
        return Name;
    }

    public String getSex() {
        return Sex;
    }

    public int getAge() {
        return Age;
    }

    public String getAdd_condition() {return add_condition;}

    public String getDate_of_birth() {return  date_of_birth;}

}
