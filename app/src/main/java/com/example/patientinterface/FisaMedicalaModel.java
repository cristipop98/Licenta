package com.example.patientinterface;

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
    private String diseases;

    public FisaMedicalaModel(String patientID, int height, int weight, String blood_type,String allergies,String intolerances,String diseases) {
        this.patientID = patientID;
        this.height = height;
        this.weight = weight;
        this.blood_type = blood_type;
        this.allergies = allergies;
        this.intolerances = intolerances;
        this.diseases = diseases;
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

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }
}
