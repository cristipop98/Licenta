package com.example.patientinterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.admininterface.DoctorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private List<MesajModel> mesajModelList;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseAuth;

    public MessageAdapter(Context context, List<MesajModel> mesajModelList) {
        this.context = context;
        this.mesajModelList = mesajModelList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==MSG_TYPE_RIGHT)
        {
            v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        else {
            v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }
        return new MessageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        MesajModel mesajModel=mesajModelList.get(position);

        holder.show_message.setText(mesajModel.getMessage());

    }

    @Override
    public int getItemCount() {
        return mesajModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            imageView=itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
       firebaseFirestore=FirebaseFirestore.getInstance();
       firebaseAuth=FirebaseAuth.getInstance().getCurrentUser();
       if(mesajModelList.get(position).getSender().equals(firebaseAuth.getUid()))
       {
           return MSG_TYPE_RIGHT;
       }
       else
       {
           return MSG_TYPE_LEFT;
       }
    }
}
