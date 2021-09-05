package com.example.model;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class ProgramareModel {
    private UUID id;
    private String doctorId;
    private String dataProgramare;
    private String pacientId;


    public ProgramareModel(String doctorId, String dataProgramare,String pacientId) {
        this.doctorId = doctorId;
        this.dataProgramare = dataProgramare;
        this.pacientId = pacientId;
    }

    public ProgramareModel()
    {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDataProgramare() {
        return dataProgramare;
    }

    public void setDataProgramare(String dataProgramare) {
        this.dataProgramare = dataProgramare;
    }

    public String getPacientId() {
        return pacientId;
    }

    public void setPacientId(String pacientId) {
        this.pacientId = pacientId;
    }
}
