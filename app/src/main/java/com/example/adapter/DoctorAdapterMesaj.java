package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.model.DoctorModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DoctorAdapterMesaj extends RecyclerView.Adapter<DoctorAdapterMesaj.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DoctorModel item);
    }

    Context context;
    private List<DoctorModel> doctorModelList;
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private OnItemClickListener listener;

    public DoctorAdapterMesaj(Context context, List<DoctorModel> doctorModelList,OnItemClickListener listener){
        this.context=context;
        this.doctorModelList=doctorModelList;
        this.listener=listener;

    }



    @NonNull
    @Override
    public DoctorAdapterMesaj.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.doctor_item_mesaj,parent,false);
        return new DoctorAdapterMesaj.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DoctorModel model=doctorModelList.get(position);
        holder.nume.setText(model.getNume());
        holder.bind(doctorModelList.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return doctorModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nume;
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nume=(TextView) itemView.findViewById(R.id.numeDoctor);
            imageView=itemView.findViewById(R.id.profile_image);




        }
        public void bind(final DoctorModel item, final DoctorAdapterMesaj.OnItemClickListener listener) {
            nume.setText("Dr."+item.getNume() + " " + item.getPrenume() + "-" + item.getSpecializare());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }


}
