package com.example.demo19;

public class Patient {
    private int idPatient;
    private String name;
    private String sex;
    private String dateOfBirth;
    private String columnResult;


    public Patient(int idPatient, String name, String sex, String dateOfBirth, String columnResult) {
        this.idPatient = idPatient;
        this.name = name;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.columnResult = columnResult;

    }

    public int getIdPatient() {
        return idPatient;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getColumnResult() {
        return columnResult;
    }


}
