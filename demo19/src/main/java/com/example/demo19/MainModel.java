package com.example.demo19;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.PublicKey;

public class MainModel {
    private ObservableList<Patient> names;

    public MainModel(){
        this.names = FXCollections.observableArrayList();
    }

}
