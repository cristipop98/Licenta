package com.example.admininterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(DoctorModel item);
    }


    Context context;
    private List<DoctorModel> doctorModelList;
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private String idBun;
    private OnItemClickListener listener;




    public DoctorAdapter(Context context, List<DoctorModel> doctorModelList,OnItemClickListener listener){
        // this.patientFragment=patientFragment;
        this.context=context;
        this.doctorModelList=doctorModelList;
        this.listener=listener;
    }


    @NonNull
    @Override
    public DoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.doctor_item,parent,false);
        return new DoctorAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.MyViewHolder holder, int position) {

        holder.nume.setText(doctorModelList.get(position).getNume());
        holder.prenume.setText(doctorModelList.get(position).getPrenume());
        holder.telefon.setText(doctorModelList.get(position).getTelefon());
        holder.mail.setText(doctorModelList.get(position).getMail());
        holder.specializare.setText(doctorModelList.get(position).getSpecializare());

        holder.bind(doctorModelList.get(position),listener);


    }

    @Override
    public int getItemCount() {
        return doctorModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nume,prenume,telefon,mail,specializare;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.nume_text_doctor);
            prenume=(TextView) itemView.findViewById(R.id.prenume_text_doctor);
            telefon=(TextView) itemView.findViewById(R.id.telefon_text_doctor);
            mail=(TextView) itemView.findViewById(R.id.mail_text_doctor);
            specializare=(TextView) itemView.findViewById(R.id.specializare_text_doctor);



        }
        public void bind(final DoctorModel item, final DoctorAdapter.OnItemClickListener listener) {
            nume.setText(item.nume);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }
}
