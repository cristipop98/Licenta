package com.example.model;

import java.util.UUID;

public class ServiciuAmbulantaModel {
    private UUID id;
    private String nume;
    private boolean disponibilitate;
    private String mail;
    private String telefon;

    public ServiciuAmbulantaModel(String nume, boolean disponibilitate, String mail,String telefon) {
        this.nume = nume;
        this.disponibilitate = disponibilitate;
        this.mail = mail;
        this.telefon=telefon;
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

    public boolean isDisponibilitate() {
        return disponibilitate;
    }

    public void setDisponibilitate(boolean disponibilitate) {
        this.disponibilitate = disponibilitate;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
