package com.example.admininterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    List<PatientModel> patientModels;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference patientCollection;
    private PatientAdapter patientAdapter;
    private View view;
    private Button btnAdauga;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }

        patientModels=new ArrayList<>();
        //CollectionReference patients=firebaseFirestore.collection("patient");
        Date data1=new Date();
        Date data2=new Date();
        SimpleDateFormat ft=new SimpleDateFormat("yyyyy-dd-mm");
        //patientModels.add(new PatientModel("Pop","Cristian", data1,"strada Cutezantei nr 20 ap 10","0789765124","crp98@gmail.com","crp98","123456"));
        //patientModels.add(new PatientModel("Popescu","Andrei", data1,"strada Infratirii nr 10 ap 15","073890762","popescu.andrei@gmail.com","popescu.andrei","123456"));

        firebaseFirestore=FirebaseFirestore.getInstance();
        showData();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_patient, container, false);
        patientAdapter=new PatientAdapter(getContext(),patientModels);
        recyclerView=(RecyclerView) view.findViewById(R.id.PatientRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(patientAdapter);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new TouchHelper(patientAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        btnAdauga=view.findViewById(R.id.btnAddPatient);



       // showData();

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddPatient.class));
            }
        });



        return view;
    }

    public void showData()
    {
        firebaseFirestore.collection("patient").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            patientModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                PatientModel model = new PatientModel(snapshot.getString("nume"), snapshot.getString("prenume"),
                                        snapshot.getString("dataNasterii"), snapshot.getString("adresa"), snapshot.getString("email"), snapshot.getString("telefon"),
                                        snapshot.getString("password"));




                                patientModels.add(model);
                            }
                            patientAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Eroare la introducerea datelor",Toast.LENGTH_SHORT).show();
            }

        });

    }




}