package com.example.praxisprojekt.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Einsicht;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MeineEinsicht extends RecyclerView.Adapter<MeineEinsicht.MyViewHolder>{
    ArrayList<Einsicht> list;  private ImageView Add_btn;
    public MeineEinsicht(ArrayList<Einsicht> list){
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_einsicht,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fach.setText(list.get(position).getFach());
        holder.Zeit.setText(list.get(position).getZeit());
        holder.raum.setText(list.get(position).getRaum());
        holder.startzeit.setText(list.get(position).getStartzeit());
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference().child("Student").child(Prevalent.StudentMatrikelnummer);
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView KlausurId, Zeit, raum, startzeit,fach;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            Zeit = itemView.findViewById(R.id.Zeit);
            fach = itemView.findViewById(R.id.fach);
            raum = itemView.findViewById(R.id.raum);
            startzeit =  itemView.findViewById(R.id.startzeit);

        }
    }
}