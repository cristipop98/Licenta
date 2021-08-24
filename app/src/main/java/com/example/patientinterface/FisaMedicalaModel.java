package com.example.patientinterface;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FisaMedicalaModel {

    private UUID id;
    private String patientID;
    private int height;
    private int weight;
    private String blood_type;
    private String allergies;
    private String intolerances;
    private String gen;
    private HashMap<String,String> diagnostic;

    public FisaMedicalaModel(String patientID, int height, int weight, String blood_type,String allergies,String intolerances,String gen,HashMap diagnostic) {
        this.patientID = patientID;
        this.height = height;
        this.weight = weight;
        this.blood_type = blood_type;
        this.allergies = allergies;
        this.intolerances = intolerances;
        this.gen=gen;
        this.diagnostic = diagnostic;
    }
    public FisaMedicalaModel()
    {}

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getIntolerances() {
        return intolerances;
    }

    public void setIntolerances(String intolerances) {
        this.intolerances = intolerances;
    }

    public HashMap<String, String> getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(HashMap<String, String> diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }
}
