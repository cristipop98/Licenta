package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddFisaMedicala extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnAdauga;
    private EditText inaltime,greutate,grupaSanguina,alergii,intolerante,diagnostic,dataConsultatie;
    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String idBun;
    private Toolbar toolbar;
    private List<String> spinnerArray;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fisa_medicala);

        inaltime=findViewById(R.id.inaltime);
        greutate=findViewById(R.id.greutate);
        grupaSanguina=findViewById(R.id.grupaSanguina);
        alergii=findViewById(R.id.alergii);
        intolerante=findViewById(R.id.intoleranta);
        diagnostic=findViewById(R.id.diagnostic);
        dataConsultatie=findViewById(R.id.dataConsultatie);
        spinner=findViewById(R.id.spinnerGen);

        btnAdauga=findViewById(R.id.btnAddFisa);
        btnBack=findViewById(R.id.imageButton);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        Intent intent1;
        intent1=getIntent();

        String idBun1=intent1.getStringExtra("ceva1");
        Log.d("Pula mia",""+idBun1);


        spinnerArray=new ArrayList<>();
        spinnerArray.add("masculin");
        spinnerArray.add("feminin");

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Gen,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaPacientiFisa.class));
            }
        });



        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=inaltime.getText().toString();
                int inaltime1=Integer.parseInt(s);
                String s1=greutate.getText().toString();
                int greutate1=Integer.parseInt(s1);
                String grupaSanguina1=grupaSanguina.getText().toString();
                String alergii1=alergii.getText().toString();
                String intoleranta1=intolerante.getText().toString();
                String diagnostic1=diagnostic.getText().toString();
                String dataConsultatie1=dataConsultatie.getText().toString();
                String gen=spinner.getSelectedItem().toString();


                AddToFirestore(idBun1,inaltime1,greutate1,grupaSanguina1,alergii1,intoleranta1,diagnostic1,dataConsultatie1,gen);



            }
        });
    }
    private void AddToFirestore(String id,int inaltime,int greutate,String grupaSanguina,String alergii,String intoleranta,String diagnostic,String dataConsultatie,String gen)
    {
        if(!id.isEmpty() && inaltime!=0 && greutate!=0 && !grupaSanguina.isEmpty() && !alergii.isEmpty()
                && !intoleranta.isEmpty() && !diagnostic.isEmpty() && !dataConsultatie.isEmpty() && !gen.isEmpty()) {
            DocumentReference collection = firebaseFirestore.collection("patient").document(id);
            HashMap<String, Object> map = new HashMap<>();
            HashMap<String,String> boli=new HashMap<>();
            boli.put(diagnostic,dataConsultatie);
            map.put("PacientID", id);
            map.put("Inaltime", inaltime);
            map.put("Greutate", greutate);
            map.put("GrupaSanguina", grupaSanguina);
            map.put("Alergii", alergii);
            map.put("Intoleranta", intoleranta);
            map.put("Diagnostic", boli);
            map.put("Gen",gen);
                firebaseFirestore.collection("fisa").document(id).set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddFisaMedicala.this, "Fisa introdusa", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ListaPacientiFisa.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFisaMedicala.this, "Eroare la introducerea datelor", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

