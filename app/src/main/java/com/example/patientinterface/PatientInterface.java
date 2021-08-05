package com.example.patientinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.R;
import com.example.logininterface.MainActivity;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PatientInterface extends AppCompatActivity {

    Button btnProgramare,btnDisconnect,btnChat,btnFisa;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_interface);

        btnDisconnect=findViewById(R.id.btnDeconectare);
        btnProgramare=findViewById(R.id.butonProgramareConsultatie);
        btnChat=findViewById(R.id.btnMesaj);
        btnFisa=findViewById(R.id.butonFisaMedicala);

        fAuth=FirebaseAuth.getInstance();

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
                startActivity(new Intent(getApplicationContext(),ListaDoctoriMesaj.class));
            }
        });

        btnFisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FisaMedicala.class));
            }
        });
    }
}