package com.example.patientinterface;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class ProgramareModel {
    UUID id;
    String doctorId;
    Date dataProgramare;
    String pacientId;


    public ProgramareModel(String doctorId, Date dataProgramare,String pacientId) {
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

    public Date getDataProgramare() {
        return dataProgramare;
    }

    public void setDataProgramare(Date dataProgramare) {
        this.dataProgramare = dataProgramare;
    }

    public String getPacientId() {
        return pacientId;
    }

    public void setPacientId(String pacientId) {
        this.pacientId = pacientId;
    }
}
