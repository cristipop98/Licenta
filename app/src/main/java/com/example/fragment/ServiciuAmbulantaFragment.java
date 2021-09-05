package com.example.fragment;

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
import com.example.activity.AddServiciuAmbulanta;
import com.example.adapter.ServiciuAmbulantaAdapter;
import com.example.model.ServiciuAmbulantaModel;
import com.example.touchHelper.TouchHelperDoctor;
import com.example.touchHelper.TouchHelperServiciuAmbulanta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiciuAmbulantaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiciuAmbulantaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    List<ServiciuAmbulantaModel> ambulantaModels;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private Button btnAdauga;
    private ServiciuAmbulantaAdapter serviciuAmbulantaAdapter;

    public ServiciuAmbulantaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiciuAmbulantaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiciuAmbulantaFragment newInstance(String param1, String param2) {
        ServiciuAmbulantaFragment fragment = new ServiciuAmbulantaFragment();
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

        ambulantaModels=new ArrayList<>();

        firebaseFirestore=FirebaseFirestore.getInstance();
        showData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_serviciu_ambulanta, container, false);
        serviciuAmbulantaAdapter=new ServiciuAmbulantaAdapter(getContext(), ambulantaModels, new ServiciuAmbulantaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ServiciuAmbulantaModel item) {
                Toast.makeText(getContext(),"Un click",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView=(RecyclerView) view.findViewById(R.id.ServiciuAmbulantaRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(serviciuAmbulantaAdapter);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new TouchHelperServiciuAmbulanta(serviciuAmbulantaAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        btnAdauga=view.findViewById(R.id.btnAddServiciuAmbulanta);

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddServiciuAmbulanta.class));
            }
        });



        return view;
    }

    public void showData()
    {
        firebaseFirestore.collection("ambulanta").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ambulantaModels.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                ServiciuAmbulantaModel model = new ServiciuAmbulantaModel(snapshot.getString("nume"), snapshot.getBoolean("disponibilitate"),
                                        snapshot.getString("email"),snapshot.getString("telefon"));




                                ambulantaModels.add(model);
                            }
                           serviciuAmbulantaAdapter.notifyDataSetChanged();
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