package com.example.demo19;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn<Patient, String> BirthColumn;
    @FXML
    private TableColumn<Patient, String> ResultColumn;
    @FXML
    private BarChart<String, Integer> chart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    protected void initialize() {
        IdPatientColumn.setCellValueFactory(new PropertyValueFactory<>("IdPatient"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        SexColumn.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        BirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        ResultColumn.setCellValueFactory(new PropertyValueFactory<>("columnResult"));
        xAxis.setLabel("Month");
        yAxis.setLabel("Positive Tests");

        retrieveChartData();
    }

    @FXML
    protected void onHelloButtonClick() {
        // Clear existing data
        covidTable.getItems().clear();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid", "root", "Q8iqQ74q@10");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.idPatient, p.Name, p.Sex, p.Date_Of_Birth, t.test_result " +
                    "FROM patient p " +
                    "INNER JOIN test t ON p.idPatient = t.patient_id");

            while (resultSet.next()) {
                int IdPatient = resultSet.getInt("idPatient");
                String Name = resultSet.getString("Name");
                String Sex = resultSet.getString("Sex");
                String BirthDate = resultSet.getString("Date_Of_Birth");
                String result = resultSet.getString("test_result");

                Patient Patient = new Patient(IdPatient, Name, Sex, BirthDate, result);
                covidTable.getItems().add(Patient);
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void retrieveChartData() {
        ObservableList<XYChart.Data<String, Integer>> chartData = FXCollections.observableArrayList();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid", "root", "Q8iqQ74q@10");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MONTH(test_date) AS month, COUNT(*) AS positive_tests " +
                    "FROM test " +
                    "WHERE YEAR(test_date) = 2022 AND test_result = 'positive' " +
                    "GROUP BY MONTH(test_date)");

            while (resultSet.next()) {
                String month = resultSet.getString("month");
                int positiveTests = resultSet.getInt("positive_tests");

                chartData.add(new XYChart.Data<>(month, positiveTests));
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setData(chartData);
        chart.getData().add(series);
    }
}
