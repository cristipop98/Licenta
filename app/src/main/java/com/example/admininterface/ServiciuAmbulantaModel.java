package com.example.admininterface;

import java.util.UUID;

public class ServiciuAmbulantaModel {
    UUID id;
    String nume;
    boolean disponibilitate;
    String mail;

    public ServiciuAmbulantaModel(String nume, boolean disponibilitate, String mail) {
        this.nume = nume;
        this.disponibilitate = disponibilitate;
        this.mail = mail;
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
}
