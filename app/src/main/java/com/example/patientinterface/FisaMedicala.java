package com.example.patientinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.R;
import com.example.admininterface.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FisaMedicala extends AppCompatActivity {

    private TextView inaltime, greutate, grupaSanguina, alergii, intolerante, boli,nume,prenume,dataNasterii,gen;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<FisaMedicalaModel> fisaMedicalaModels;
    private String idBun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisa_medicala);

        inaltime = findViewById(R.id.inaltimeFisa);
        greutate = findViewById(R.id.greutateFisa);
        grupaSanguina = findViewById(R.id.grupaSanguinaFisa);
        alergii = findViewById(R.id.alergiiFisa);
        intolerante = findViewById(R.id.intoleranteFisa);
        boli = findViewById(R.id.boliFisa);
        nume=findViewById(R.id.numeFisa);
        prenume=findViewById(R.id.prenumeFisa);
        gen=findViewById(R.id.genFisa);

        fisaMedicalaModels=new ArrayList<>();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        showData();


    }


    public void showData() {
        firebaseFirestore.collection("fisa").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String,String> ceva=new HashMap<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                FisaMedicalaModel model = new FisaMedicalaModel(snapshot.getString("PacientID").toString(), snapshot.getLong("Inaltime").intValue(),
                                        snapshot.getLong("Greutate").intValue(), snapshot.getString("GrupaSanguina"), snapshot.getString("Alergii"), snapshot.getString("Intoleranta"),
                                        snapshot.getString("Gen"),
                                        (HashMap) snapshot.get("Diagnostic"));
                                ceva=(HashMap)snapshot.get("Diagnostic");


                                fisaMedicalaModels.add(model);


                                if(model.getPatientID().equals(firebaseAuth.getCurrentUser().getUid()))
                                {
                                    HashMap<String, String> finalCeva = ceva;
                                    HashMap<String, String> finalCeva1 = ceva;
                                    firebaseFirestore.collection("patient").document(model.getPatientID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists())
                                            {
                                                nume.setText(documentSnapshot.getString("nume") + " " + documentSnapshot.getString("prenume"));
                                                prenume.setText(documentSnapshot.getString("dataNasterii"));

                                                inaltime.setText(model.getHeight() + "");
                                                greutate.setText(model.getWeight() + "");
                                                grupaSanguina.setText(model.getBlood_type());
                                                alergii.setText(model.getAllergies());
                                                intolerante.setText(model.getIntolerances());
                                                gen.setText(model.getGen());
                                                for(Map.Entry<String,String> entry: finalCeva1.entrySet())
                                                {
                                                    String key=entry.getKey();
                                                    String value=entry.getValue();
                                                    boli.setText(key + "" + value);
                                                }

                                            }
                                        }
                                    });



                                }

                            }
                        }
                    }
                });//.addOnFailureListener(new OnFailureListener() {
        //  @Override
        // public void onFailure(@NonNull Exception e) {
        //   Toast.makeText(getActivity(),"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
        //}

        //});

                }
    }