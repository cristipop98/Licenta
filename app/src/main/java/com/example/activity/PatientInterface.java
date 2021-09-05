package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class PatientInterface extends AppCompatActivity {

    Button btnProgramare,btnDisconnect,btnChat,btnFisa,btnUrgenta,btnPrescriptie;
    FirebaseAuth fAuth;
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_interface);

        btnDisconnect=findViewById(R.id.btnDeconectare);
        btnProgramare=findViewById(R.id.butonProgramareConsultatie);
        btnChat=findViewById(R.id.btnMesaj);
        btnFisa=findViewById(R.id.butonFisaMedicala);
        btnUrgenta=findViewById(R.id.butonUrgentaMedicala);
        btnPrescriptie=findViewById(R.id.butonMedicamente);

        fAuth=FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().subscribeToTopic("patient");

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fAuth.getCurrentUser()!=null)
                {
                    fAuth.getInstance().signOut();
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();


            }
        });

        btnProgramare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaDoctoriProgramare.class));
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaDoctoriMesaj.class));
            }
        });

        btnFisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FisaMedicala.class);
                intent.putExtra("type", "patient");
                startActivity(intent);
                //startActivity(new Intent(getApplicationContext(), FisaMedicala.class));
            }
        });

        btnUrgenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UrgentaMedicala.class));
            }
        });

        btnPrescriptie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PrescriptieMedicala.class));
            }
        });
    }
}