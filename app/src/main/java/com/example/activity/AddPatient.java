package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.R;
import com.example.model.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPatient extends AppCompatActivity {

    private Button btnAdauga;
    private EditText nume,prenume,dataNasterii,adresa,email,telefon,parola;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String uNume,uPrenume,uDataNasterii,uAdresa,uEmail,uTelefon,uId;
    private String id;
    private ArrayList PatientIdList;
    private CollectionReference itemref;
    private String idBun;
    private Toolbar toolbar;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        nume=findViewById(R.id.pAddNume);
        prenume=findViewById(R.id.pAddPrenume);
        dataNasterii=findViewById(R.id.pAddDataNasterii);
        adresa=findViewById(R.id.pAddAdresa);
        email=findViewById(R.id.pAddEmail);
        telefon=findViewById(R.id.pAddTelefon);
        parola=findViewById(R.id.pAddParolaUtilizator);
        btnAdauga=findViewById(R.id.buttonAdauga);
        toolbar=findViewById(R.id.toolbarAdaugaPacient);
        btnBack=findViewById(R.id.imageButton);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null)
        {
            btnAdauga.setText("Update");
            toolbar.setTitle("Editeaza pacient");
            uId=bundle.getString("id");
            uNume=bundle.getString("nume");
            uPrenume=bundle.getString("prenume");
            uDataNasterii=bundle.getString("dataNasterii");
            uAdresa=bundle.getString("adresa");
            uEmail=bundle.getString("email");
            uTelefon=bundle.getString("telefon");

            nume.setText(uNume);
            prenume.setText(uPrenume);
            dataNasterii.setText(uDataNasterii);
            adresa.setText(uAdresa);
            email.setText(uTelefon);
            telefon.setText(uEmail);





        }
        else
        {
            btnAdauga.setText("Adauga");


        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Admin.class));
            }
        });

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume1=nume.getText().toString();
                String prenume1=prenume.getText().toString();
                String dataNasterii1=dataNasterii.getText().toString();
                String adresa1=adresa.getText().toString();
                String email1=email.getText().toString();
                String telefon1=telefon.getText().toString();
                String parola1=parola.getText().toString();

                Bundle bundle1=getIntent().getExtras();

                if(bundle1!=null)
                {


                    readId(email1,new FirestoreCallback(){
                        @Override
                        public void onCallback(String id) {
                            UpdateToFireStore(id,nume1,prenume1,dataNasterii1,adresa1,email1,telefon1);
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

                            AddToFirestore(id,nume1,prenume1,dataNasterii1,adresa1,email1,telefon1,parola1);


                        }

                    }
                });


                }


            }
        });



    }

    private void UpdateToFireStore(String id,String nume,String prenume,String dataNasterii,String adresa,String email,String telefon)
    {

        firebaseFirestore.collection("patient").document(id).update("nume",nume,"prenume",prenume,"dataNasterii",dataNasterii,"adresa",adresa,"email",email,"telefon",telefon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddPatient.this,"Pacient actualizat", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Admin.class));

                        }
                        else
                        {
                            Toast.makeText(AddPatient.this,"Eroare la actualizarea datelor" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPatient.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void AddToFirestore(String id,String nume1,String prenume1,String dataNasterii1,String adresa1,String email1,String telefon1,String parola1)
    {
        if(!nume1.isEmpty() && !prenume1.isEmpty() && !dataNasterii1.isEmpty() && !adresa1.isEmpty() && !email1.isEmpty() && !telefon1.isEmpty() && !parola1.isEmpty())
        {
            HashMap<String,Object> map=new HashMap<>();
            map.put("nume",nume1);
            map.put("prenume",prenume1);
            map.put("dataNasterii",dataNasterii1);
            map.put("adresa",adresa1);
            map.put("email",email1);
            map.put("telefon",telefon1);

            HashMap<String,Object> mapUser=new HashMap<>();
            mapUser.put("email",email1);
            mapUser.put("password",parola1);
            mapUser.put("type","patient");

            firebaseFirestore.collection("users").document(id).set(mapUser);

            firebaseFirestore.collection("patient").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AddPatient.this,"Pacient introdus",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Admin.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPatient.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            Toast.makeText(this,"You must complete all fields",Toast.LENGTH_SHORT).show();
        }
    }


    private void readId(String email,FirestoreCallback firestoreCallback)
    {

        idBun=new String();

        firebaseFirestore.collection("patient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        PatientModel model = new PatientModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                                snapshot.getString("dataNasterii"), snapshot.getString("adresa"), snapshot.getString("email"), snapshot.getString("telefon"));


                        //if(email!=null)
                       // Log.d("email",email);
                       // if(model.getTelefon()!=null)
                         //   Log.d("email1",model.getTelefon());

                        if(model.getTelefon().equals(email))
                        {
                            String id=snapshot.getId();
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