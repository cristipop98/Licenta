package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.R;
import com.example.adapter.PrescriptieAdapter;
import com.example.model.PrescriptieModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriptieMedicala extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date dateObject,timeObject;
    int x=0;
    private List<PrescriptieModel> prescriptieModels;
    PrescriptieAdapter prescriptieAdapter;
    RecyclerView recyclerView;
    Button prescriptie;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptie_medicala);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        prescriptieModels=new ArrayList<>();

        prescriptieAdapter=new PrescriptieAdapter(PrescriptieMedicala.this,prescriptieModels);

        prescriptie=findViewById(R.id.btnAddPrescriptie);
        btnBack=findViewById(R.id.imageButton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PatientInterface.class));
            }
        });

        recyclerView=findViewById(R.id.RecycleViewListaPrescriptiiPatient);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(prescriptieAdapter);


        showPrescriptie(firebaseAuth.getCurrentUser().getUid());
    }

    public void showPrescriptie(String id)
    {
        firebaseFirestore.collection("prescriptie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            prescriptieModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                PrescriptieModel model = new PrescriptieModel(snapshot.getString("Nume"), snapshot.getString("EfecteSecundare"),
                                        snapshot.getString("Administrare"),snapshot.getLong("Pret").intValue(),snapshot.getString("PacientID"));

                                if(model.getPatientID().equals(id))
                                    prescriptieModels.add(model);
                            }
                            prescriptieAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PrescriptieMedicala.this,"Eroare la afisarea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }
}