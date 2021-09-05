package com.example.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

public class PrescriptieModel {
    private UUID id;
    private String nume;
    private String efecteSecundare;
    private String administrare;
    private int pret;
    private String patientID;

    public PrescriptieModel(String nume, String efecteSecundare, String administrare, int pret, String patientID) {
        this.nume = nume;
        this.efecteSecundare = efecteSecundare;
        this.administrare = administrare;
        this.pret = pret;
        this.patientID = patientID;
    }

    public PrescriptieModel()
    {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEfecteSecundare() {
        return efecteSecundare;
    }

    public void setEfecteSecundare(String efecteSecundare) {
        this.efecteSecundare = efecteSecundare;
    }

    public String getAdministrare() {
        return administrare;
    }

    public void setAdministrare(String administrare) {
        this.administrare = administrare;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
}
