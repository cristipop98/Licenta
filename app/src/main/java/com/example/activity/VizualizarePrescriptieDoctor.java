package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.example.adapter.ProgramareAdapter;
import com.example.model.PrescriptieModel;
import com.example.model.ProgramareModel;
import com.example.touchHelper.TouchHelperDoctor;
import com.example.touchHelper.TouchHelperPrescriptie;
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

public class VizualizarePrescriptieDoctor extends AppCompatActivity {

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
        setContentView(R.layout.activity_vizualizare_prescriptie_doctor);

        btnBack=findViewById(R.id.imageButton);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        String idBun1=intent.getStringExtra("ID");

        prescriptieModels=new ArrayList<>();

        prescriptieAdapter=new PrescriptieAdapter(VizualizarePrescriptieDoctor.this,prescriptieModels);

        prescriptie=findViewById(R.id.btnAddPrescriptie);

        recyclerView=findViewById(R.id.RecycleViewListaPrescriptii);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(prescriptieAdapter);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new TouchHelperPrescriptie(prescriptieAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DoctorInterface.class));
            }
        });



        showPrescriptie(idBun1);

        prescriptie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), AddPrescriptie.class);
                intent1.putExtra("ID", idBun1);
                startActivity(intent1);

            }
        });
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
                Toast.makeText(VizualizarePrescriptieDoctor.this,"Eroare la afisarea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }
    }