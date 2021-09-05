package com.example.model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.UUID;

public class PatientModel {
    private UUID id;
    private String nume;
    private String prenume;
    private String dataNasterii;
    private String adresa;
    private String telefon;
    private String email;

    public PatientModel(String nume, String prenume, String dataNasterii, String adresa, String telefon, String email) {
        this.nume = nume;
        this.prenume = prenume;
        this.dataNasterii = dataNasterii;
        this.adresa = adresa;
        this.telefon = telefon;
        this.email = email;
    }
    public PatientModel()
    {}

    @Exclude
    public UUID getIdd() {
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

    public String getDataNasterii() {
        return dataNasterii;
    }

    public void setDataNasterii(String dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
