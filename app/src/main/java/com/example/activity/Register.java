package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.model.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    EditText nume,prenume,dataNasterii,email,adresa,telefon,parolaUtilizator;
    Button butonCreare;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nume=findViewById(R.id.pAddNume);
        prenume=findViewById(R.id.pAddPrenume);
        dataNasterii=findViewById(R.id.pAddDataNasterii);
        adresa=findViewById(R.id.pAddAdresa);
        email=findViewById(R.id.pAddEmail);
        telefon=findViewById(R.id.pAddTelefon);
        parolaUtilizator=findViewById(R.id.pAddParolaUtilizator);

        butonCreare=findViewById(R.id.buttonAdauga);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        butonCreare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString().trim();
                String parola=parolaUtilizator.getText().toString().trim();
                String name=nume.getText().toString();
                String prename=prenume.getText().toString();
                String birthDate=dataNasterii.getText().toString();
                String adress=adresa.getText().toString();
                String phone=telefon.getText().toString();

                if(TextUtils.isEmpty(mail)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(parola)){
                    parolaUtilizator.setError("Password is Required");
                    return;
                }

                if(parolaUtilizator.length()<6){
                    parolaUtilizator.setError("Password must be at least 6 characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(mail,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Register.this,"User created",Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("patient").document(userID);
                            Map<String,Object> patient=new HashMap<>();
                            patient.put("nume",name);
                            patient.put("prenume",prename);
                            patient.put("dataNasterii",birthDate);
                            patient.put("adresa",adress);
                            patient.put("telefon",phone);
                            patient.put("email",mail);
                            DocumentReference documentReference1=fStore.collection("users").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("email",mail);
                            user.put("type","patient");
                            documentReference1.set(user);
                            documentReference.set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","onSuccess:patient Profile is created for" +userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this,"Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });



    }
}