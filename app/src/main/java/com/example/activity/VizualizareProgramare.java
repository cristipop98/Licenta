package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.R;
import com.example.adapter.ProgramareAdapter;
import com.example.model.ProgramareModel;
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

public class VizualizareProgramare extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date dateObject,timeObject;
    int x=0;
    private List<ProgramareModel> programareModels;
    ProgramareAdapter programareAdapter;
    RecyclerView recyclerView;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizare_programare);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        btnBack=findViewById(R.id.imageButton);

        programareModels=new ArrayList<>();

        programareAdapter=new ProgramareAdapter(VizualizareProgramare.this,programareModels);

        recyclerView=findViewById(R.id.RecycleViewListaProgramare);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(programareAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DoctorInterface.class));
            }
        });


        showProgramari(firebaseAuth.getCurrentUser().getUid());
    }

    public void showProgramari(String id)
    {
        firebaseFirestore.collection("programare").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            programareModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                ProgramareModel model = new ProgramareModel(snapshot.getString("doctorId"), snapshot.getString("dataProgramare"),
                                        snapshot.getString("pacientId"));

                                if(model.getDoctorId().equals(id))
                                    programareModels.add(model);
                            }
                            programareAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VizualizareProgramare.this,"Eroare la afisarea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }
}