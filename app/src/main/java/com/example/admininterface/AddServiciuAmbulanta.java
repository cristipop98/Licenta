package com.example.admininterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AddServiciuAmbulanta extends AppCompatActivity {

    private Button btnAdauga;
    private EditText nume,mail,parola,telefon;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String idBun,id;
    private String uNume,uMail,uParola,uId,uTelefon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_serviciu_ambulanta);

        nume=findViewById(R.id.addAmbulantaNume);
        mail=findViewById(R.id.addAmbulantaMail);
        parola=findViewById(R.id.addAmbulantaParola);
        telefon=findViewById(R.id.addAmbulantaTelefon);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        btnAdauga=findViewById(R.id.buttonAddAmbulanta);

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null) {
            btnAdauga.setText("Update");
            uId = bundle.getString("id");
            uNume = bundle.getString("nume");
            uMail = bundle.getString("email");
            uTelefon=bundle.getString("telefon");

            nume.setText(uNume);
            mail.setText(uMail);
        }
        else
        {
            btnAdauga.setText("Adauga");
        }


        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume1=nume.getText().toString();
                String email1=mail.getText().toString();
                String telefon1=telefon.getText().toString();
                String parola1=parola.getText().toString();

                Bundle bundle1=getIntent().getExtras();

                if(bundle1!=null)
                {

                    // showId(uEmail);
                    readId(email1,new AddServiciuAmbulanta.FirestoreCallback() {
                        @Override
                        public void onCallback(String idBun) {
                            UpdateToFireStore(id,nume1,email1,telefon1);
                        }
                    });
                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(email1,parola1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user=task.getResult().getUser();

                                String id=user.getUid();

                                AddToFirestore(id,nume1,true,email1,telefon1,parola1);


                            }

                        }
                    });


                }

            }
        });


    }

    private void UpdateToFireStore(String id,String nume,String email,String telefon)
    {

        firebaseFirestore.collection("ambulanta").document(id).update("nume",nume,"email",email,"telefon",telefon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddServiciuAmbulanta.this,"Serciviu de ambulanta actualizat", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Admin.class));

                        }
                        else
                        {
                            Toast.makeText(AddServiciuAmbulanta.this,"Eroare la actualizarea datelor" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddServiciuAmbulanta.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void AddToFirestore(String id,String nume1,Boolean disponibilitate1,String email1,String telefon1,String parola1)
    {
        if(!nume1.isEmpty()   && !email1.isEmpty()  && !parola1.isEmpty() && !telefon1.isEmpty())
        {
            HashMap<String,Object> map=new HashMap<>();
            // map.put("id",id);
            map.put("nume",nume1);
            map.put("disponibilitate",disponibilitate1);
            map.put("email",email1);
            map.put("telefon",telefon1);

            HashMap<String,Object> mapUser=new HashMap<>();
            mapUser.put("email",email1);
            mapUser.put("password",parola1);
            mapUser.put("type","ambulanta");

            firebaseFirestore.collection("users").document(id).set(mapUser);

            firebaseFirestore.collection("ambulanta").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AddServiciuAmbulanta.this,"Ambulanta introdusa",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Admin.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddServiciuAmbulanta.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            Toast.makeText(this,"You must complete all fields",Toast.LENGTH_SHORT).show();
        }
    }

    private void readId(String email, AddServiciuAmbulanta.FirestoreCallback firestoreCallback)
    {
        //  PatientIdList=new ArrayList();
        idBun=new String();

        firebaseFirestore.collection("ambulanta").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        ServiciuAmbulantaModel model = new ServiciuAmbulantaModel(snapshot.getString("nume"), snapshot.getBoolean("disponibilitate"),
                                snapshot.getString("email"),snapshot.getString("telefon"));

                        if(model.getMail().equals(email))
                        {
                            String id=snapshot.getId();
                            //Log.d("id1",id);
                            idBun=id;

                        }
                    }
                    firestoreCallback.onCallback(idBun);
                }
            }
        });

    }
    private interface FirestoreCallback{
        void onCallback(String idBun);
    }
}