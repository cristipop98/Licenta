package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.R;
import com.example.adapter.ProgramareAdapter;
import com.example.model.DoctorModel;
import com.example.model.ProgramareModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static java.security.AccessController.getContext;

public class CalendarProgramare extends AppCompatActivity {


    EditText date_time_in;
    Button btnPorgramare;
    ImageButton btnBack;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private List<ProgramareModel> programareModels;

    private boolean mIgnoreEvent=false;
    ProgramareAdapter programareAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        date_time_in=findViewById(R.id.textDateHour);

        date_time_in.setInputType(InputType.TYPE_NULL);


        Intent intent=getIntent();
        String idBun1=intent.getStringExtra("ID");

        btnPorgramare=findViewById(R.id.btnAdaugaProgramare);
        btnBack=findViewById(R.id.imageButton);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        programareModels=new ArrayList<>();

        programareAdapter=new ProgramareAdapter(CalendarProgramare.this,programareModels);

        recyclerView=findViewById(R.id.recycleViewProgramare);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(programareAdapter);

        showProgramari(idBun1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PatientInterface.class));
            }
        });






        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });


        btnPorgramare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctorId=idBun1;
                String pacientId= firebaseAuth.getUid();
                String dataProgramare=date_time_in.getText().toString();
                String id=UUID.randomUUID().toString();

                showData();
                if(programareModels.size()==0)
                {
                    AddToFirestore(id, dataProgramare, doctorId, pacientId);
                }
                else {

                    for (ProgramareModel i : programareModels) {
                        if (i.getDataProgramare().toString().equals(dataProgramare)) {
                            Toast.makeText(CalendarProgramare.this, "Exista o programare la aceasta ora", Toast.LENGTH_SHORT).show();
                        } else {
                            AddToFirestore(id, dataProgramare, doctorId, pacientId);
                        }
                    }
                }


            }
        });
    }

    private void showDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        long time=System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yy-MM-dd");
        simpleDateFormat1.format(time);
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute>=0 && minute <30)
                            minute=0;
                        else if (minute >=30 && minute<=59)
                            minute=30;
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                        Log.d("Timp",simpleDateFormat1.toString());

                        if(calendar.getTimeInMillis()<(time))
                        {
                            Toast.makeText(CalendarProgramare.this,"Selectati o data valida",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {

                            if (hourOfDay >= 16 || hourOfDay < 8) {
                                Toast.makeText(CalendarProgramare.this, "Interval depasit", Toast.LENGTH_SHORT).show();
                            } else {
                                date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }
                    }
                };
                new TimePickerDialog(CalendarProgramare.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY)
                        ,calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(CalendarProgramare.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                Toast.makeText(CalendarProgramare.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void AddToFirestore(String id,String dataProgramare,String doctorId,String pacientId)
    {
        if(!dataProgramare.isEmpty() && !doctorId.isEmpty() && !pacientId.isEmpty())
        {
            HashMap<String,Object> map=new HashMap<>();
            map.put("dataProgramare",dataProgramare);
            map.put("doctorId",doctorId);
            map.put("pacientId",pacientId);


            firebaseFirestore.collection("programare").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(CalendarProgramare.this,"Programare introdusa",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ListaDoctoriProgramare.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CalendarProgramare.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            Toast.makeText(this,"You must complete all fields",Toast.LENGTH_SHORT).show();
        }
    }

    public void showData()
    {
        firebaseFirestore.collection("programare").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            programareModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                ProgramareModel model = new ProgramareModel(snapshot.getString("pacientId"), snapshot.getString("dataProgramare"),
                                        snapshot.getString("doctorId"));

                                programareModels.add(model);

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CalendarProgramare.this,"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }



}
