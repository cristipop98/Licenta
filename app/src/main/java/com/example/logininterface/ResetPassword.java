package com.example.logininterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.admininterface.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button btnResetareParola;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText=findViewById(R.id.editTextEmail);
        btnResetareParola=findViewById(R.id.btnResetareParola);

        fAuth=FirebaseAuth.getInstance();

        btnResetareParola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });


    }

    private void resetPassword(){
        String email=emailEditText.getText().toString().trim();
        if(email.isEmpty())
        {
            emailEditText.setError("Introduceti adresa de mail");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {

            emailEditText.setError("Introduceti o adresa de mail valida");
            emailEditText.requestFocus();
            return;
        }


        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ResetPassword.this,"Verificati mailul pentru resetarea parolei",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(ResetPassword.this,"A aparut o eroare.Incercati din nou",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}