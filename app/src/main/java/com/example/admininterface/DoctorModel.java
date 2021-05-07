package com.example.admininterface;

import java.util.UUID;

public class DoctorModel {
    UUID id;
    String nume;
    String prenume;
    String telefon;
    String mail;
    String specializare;

    public DoctorModel(String nume, String prenume, String telefon,String mail, String specializare) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
        this.mail=mail;
        this.specializare = specializare;
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

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSpecializare() {
        return specializare;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }
}
