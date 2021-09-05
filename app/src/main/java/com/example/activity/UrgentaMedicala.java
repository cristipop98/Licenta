package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UrgentaMedicala extends AppCompatActivity {

    Button btnTrimite,btnLocatie;
    private EditText textDescriere;
    private String descriere;
    private RequestQueue requestQueue;
    private String URL="https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgenta_medicala);

        btnTrimite=findViewById(R.id.buttonTrimiteNotificare);
        btnLocatie=findViewById(R.id.buttonLocatie);
        textDescriere=findViewById(R.id.txtDescriere);


        requestQueue= Volley.newRequestQueue(this);

        FirebaseMessaging.getInstance().subscribeToTopic("ambulanta");


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();



                    }
                });





        btnTrimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    descriere=textDescriere.getText().toString();
                    sendNotification();

            }
        });

        btnLocatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Location.class));

            }
        });
    }



    private void sendNotification(){
        JSONObject object=new JSONObject();
        try {
            object.put("to", "/topics/" + "ambulanta");
            JSONObject notificationObject=new JSONObject();
            notificationObject.put("title","Urgenta");
            notificationObject.put("body",descriere);

            JSONObject extraData=new JSONObject();
            extraData.put("Informatie suplimentara","info");
            extraData.put("alta informatie","info ceva");

            object.put("notification",notificationObject);
            object.put("data",extraData);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError{
                    Map<String,String> header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAUAmD3Do:APA91bFabCO-YYLIvwlXub7MOghZYYWgab9EPgkkmz_OYi2RmOL5X5BJK54JidayC0tISb-DSwt3_pL8eiMP8OFDIrOsvywPydLzVDYKoCFy0hS3OZaLKmRHx1BAaWp6LODdNrlLIk42");
                    return header;
                }

            };
            requestQueue.add(request);

        } catch (JSONException e){
            e.printStackTrace();
        }

    }




}