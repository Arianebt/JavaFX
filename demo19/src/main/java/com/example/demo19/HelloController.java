package com.example.demo19;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.*;

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
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton sexRadioButton;
    @FXML
    private RadioButton prognosticRadioButton;
    @FXML
    private BarChart<String, Integer> chart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private PieChart pieChart;
    @FXML
    private ObservableList<XYChart.Data<String, Integer>> chartData = FXCollections.observableArrayList();


    @FXML
    private void handleRadioButtonAction(ActionEvent event) {
        if (event.getSource() == monthRadioButton && monthRadioButton.isSelected()) {
            updateChartByMonth();
        } else if (event.getSource() == sexRadioButton && sexRadioButton.isSelected()) {
            updateChartBySex();
        } else if (event.getSource() == prognosticRadioButton && prognosticRadioButton.isSelected()) {
            updateChartByPrognostic();
        }
    }

    @FXML
    protected void initialize() {
        IdPatientColumn.setCellValueFactory(new PropertyValueFactory<>("IdPatient"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        SexColumn.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        BirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        ResultColumn.setCellValueFactory(new PropertyValueFactory<>("columnResult"));

        ToggleGroup toggleGroup = new ToggleGroup();
        monthRadioButton.setToggleGroup(toggleGroup);
        sexRadioButton.setToggleGroup(toggleGroup);
        prognosticRadioButton.setToggleGroup(toggleGroup);

         toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearChart();
            } else if (newValue == monthRadioButton) {
                updateChartByMonth();
            } else if (newValue == sexRadioButton) {
                updateChartBySex();
            } else if (newValue == prognosticRadioButton) {
                updateChartByPrognostic();
            }
        });

        yAxis.setLabel("Positive Tests");
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

    private void updateChartByMonth() {
        clearChart();

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

        // Sort the chart data based on the month in ascending order
        chartData.sort((data1, data2) -> {
            int month1 = Integer.parseInt(data1.getXValue());
            int month2 = Integer.parseInt(data2.getXValue());
            return Integer.compare(month1, month2);
        });

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setData(chartData);
        chart.getData().add(series);
        xAxis.setLabel("Month");
    }

    private void updateChartBySex() {
        clearChart();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid", "root", "Q8iqQ74q@10");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Sex, COUNT(*) AS positive_tests " +
                    "FROM patient p " +
                    "INNER JOIN test t ON p.idPatient = t.patient_id " +
                    "WHERE YEAR(t.test_date) = 2022 AND t.test_result = 'positive' " +
                    "GROUP BY Sex");

            while (resultSet.next()) {
                String sex = resultSet.getString("Sex");
                int positiveTests = resultSet.getInt("positive_tests");

                chartData.add(new XYChart.Data<>(sex, positiveTests));
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setData(chartData);
        chart.getData().add(series);
        xAxis.setLabel("Sex");
    }

    private void updateChartByPrognostic() {
        clearChart();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid", "root", "Q8iqQ74q@10");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT prognostic, COUNT(*) AS positive_tests " +
                    "FROM caseresolution  c " +
                    "INNER JOIN test t ON c.Patient_id = t.patient_id " +
                    "WHERE YEAR(t.test_date) = 2022 AND t.test_result = 'positive' " +
                    "GROUP BY prognostic");

            while (resultSet.next()) {
                String prognostic = resultSet.getString("prognostic");
                int positiveTests = resultSet.getInt("positive_tests");

                chartData.add(new XYChart.Data<>(prognostic, positiveTests));
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setData(chartData);
        chart.getData().add(series);
        xAxis.setLabel("Prognostic");
    }

    @FXML
    protected void handleClearButtonAction(ActionEvent event) {
        covidTable.getItems().clear();
        clearChart();
    }

    private void clearChart() {
        chart.getData().clear();
        chartData.clear();
    }

    @FXML
    private void handlePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                clearChart();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid", "root", "Q8iqQ74q@10");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MONTH(t.test_date) AS month, COUNT(*) AS positive_tests " +
                    "FROM caseresolution c " +
                    "INNER JOIN test t ON c.Patient_id = t.patient_id " +
                    "WHERE YEAR(t.test_date) = 2022 AND t.test_result = 'positive' " +
                    "GROUP BY MONTH(t.test_date) ORDER BY month ASC");  // Order by month in ascending order


            while (resultSet.next()) {
                String month = resultSet.getString("month");
                int positiveTests = resultSet.getInt("positive_tests");

                pieChartData.add(new PieChart.Data(month, positiveTests));
            }

            pieChart.setData(pieChartData);

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

}


