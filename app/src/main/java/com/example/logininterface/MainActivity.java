package com.example.logininterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.admininterface.Admin;
import com.example.doctorinterface.DoctorInterface;
import com.example.patientinterface.PatientInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    EditText email,parolaUtilizator;
    Button butonLogare,btnCreareCont;
    FirebaseAuth fAuth;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseAuth.getInstance().signOut();

        email=findViewById(R.id.usernameLogin);
        parolaUtilizator=findViewById(R.id.passwordLogin);

        butonLogare=findViewById(R.id.Autentificare);
        btnCreareCont=findViewById(R.id.Creare_cont);
        forgotPassword=findViewById(R.id.textResetPassword);

        fAuth=FirebaseAuth.getInstance();

        butonLogare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString().trim();
                String parola=parolaUtilizator.getText().toString().trim();

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


                fAuth.signInWithEmailAndPassword(mail,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore ref=FirebaseFirestore.getInstance();
                            CollectionReference userRef=ref.collection("users");
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            userRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        DocumentSnapshot document=task.getResult();
                                        if(document.exists())
                                        {
                                            String type=document.getString("type");
                                            if(type.equals("doctor"))
                                            {
                                                Toast.makeText(MainActivity.this, "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), DoctorInterface.class));
                                            }
                                            else if(type.equals("patient"))
                                            {
                                                Toast.makeText(MainActivity.this, "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), PatientInterface.class));
                                            }
                                            else if(type.equals("admin"))
                                            {
                                                Toast.makeText(MainActivity.this, "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Admin.class));
                                            }

                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Account does not exist" ,Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Email/Password incorrect" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
});

        btnCreareCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPassword.class));
            }
        });
    }
}