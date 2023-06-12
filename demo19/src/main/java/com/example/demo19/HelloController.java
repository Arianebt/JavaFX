package com.example.demo19;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloController {
    @FXML
    private TableView<Patient> covidTable;
    @FXML
    private TableColumn<Patient, Integer> IdPatientColumn;
    @FXML
    private TableColumn<Patient, String> NameColumn;
    @FXML
    private TableColumn<Patient, String> SexColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;

    @FXML
    private TableColumn<Patient, String> BirthColumn;

    @FXML
    private TableColumn<Patient, String> ConditionColumn;

    @FXML
    protected void initialize() {
        IdPatientColumn.setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        SexColumn.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
        BirthColumn.setCellValueFactory(new PropertyValueFactory<>("Date_Of_Brith"));
        ConditionColumn.setCellValueFactory(new PropertyValueFactory<>("Add_condition"));

    }

    @FXML
    protected void onHelloButtonClick() {
        // Clear existing data
        covidTable.getItems().clear();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid", "root", "Q8iqQ74q@10");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT idPatient, Name, Sex, Age, Date_of_Birth, Add_Condition FROM patient");

            while (resultSet.next()) {
                int IdPatient = resultSet.getInt("idPatient");
                String Name = resultSet.getString("Name");
                String Sex = resultSet.getString("Sex");
                int Age = resultSet.getInt("Age");
                String BirthDate = resultSet.getString("Date_Of_Brith");
                String Prev_Condition = resultSet.getString("Add_condition");

                Patient Patient = new Patient(IdPatient, Name, Sex, Age, BirthDate, Prev_Condition);
                covidTable.getItems().add(Patient);
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
