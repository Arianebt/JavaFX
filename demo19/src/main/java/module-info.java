module com.example.demo19 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.demo19 to javafx.fxml;
    exports com.example.demo19;
}