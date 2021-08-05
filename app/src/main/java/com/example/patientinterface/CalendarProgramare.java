package com.example.patientinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.R;
import com.example.admininterface.AddPatient;
import com.example.admininterface.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CalendarProgramare extends AppCompatActivity {

    EditText date_in;
    EditText time_in;
    EditText date_time_in;
    Button btnPorgramare;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date dateObject,timeObject;
    int x=0;

    private static final int TIME_PICKER_INTERVAL=15;
    private boolean mIgnoreEvent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        date_in=findViewById(R.id.textDate);
        time_in=findViewById(R.id.textHour);
        date_time_in=findViewById(R.id.textDateHour);

        date_time_in.setInputType(InputType.TYPE_NULL);
        date_in.setInputType(InputType.TYPE_NULL);
        time_in.setInputType(InputType.TYPE_NULL);

        Intent intent=getIntent();
        String idBun1=intent.getStringExtra("ID");

        btnPorgramare=findViewById(R.id.btnAdaugaProgramare);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


       // date_time_in.setText(idBun1);

        date_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(date_in);
            }
        });

        time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(time_in);
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
                String dataProgramare= date_in.getText().toString();
                String oraProgramare=time_in.getText().toString();
                String oraProgramareEnd=date_time_in.getText().toString();
                String id=UUID.randomUUID().toString();


              AddToFirestore(id,dataProgramare,oraProgramare,oraProgramareEnd,doctorId,pacientId);

                /*

                DateFormat formatter = new SimpleDateFormat("yy/MM/dd");
                DateFormat formatter1=new SimpleDateFormat("h:mm");

                try {
                    dateObject=formatter.parse(dataProgramare);
                    timeObject=formatter1.parse(oraProgramare);
                    Log.d("Data",dateObject.toString() + timeObject.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                */


            }
        });
    }

    private void showDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(CalendarProgramare.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(CalendarProgramare.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimeDialog(final EditText time_in) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }

        };

        new TimePickerDialog(CalendarProgramare.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void showDateDialog(final EditText date_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(CalendarProgramare.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void AddToFirestore(String id,String dataProgramare,String oraProgramareStart,String oraProgramareEnd,String doctorId,String pacientId)
    {
        if(!dataProgramare.isEmpty() && !oraProgramareStart.isEmpty() && !oraProgramareEnd.isEmpty() && !doctorId.isEmpty() && !pacientId.isEmpty())
        {
            HashMap<String,Object> map=new HashMap<>();
          //  map.put("id",id);
            map.put("dataProgramare",dataProgramare);
            map.put("oraProgramareStart",oraProgramareStart);
            map.put("oraProgramareEnd",oraProgramareEnd);
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



}