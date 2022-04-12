package com.example.praxisprojekt.professor.klausurtermin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.Service.FirebaseDatabaseHelper;
import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Klausur;
import com.example.praxisprojekt.professor.HomepageProfActivity;
import com.example.praxisprojekt.model.Professor;
import com.example.praxisprojekt.student.KlausurListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KlausurtermineProf extends AppCompatActivity {
    public static String namefach;
    DatabaseReference ref;
    ArrayList<Klausur> list;
    RecyclerView recyclerView;
    SearchView searchView;
    Button add_kl;
    ImageView back_kl;
    FirebaseDatabaseHelper helper;
    KlausurListAdapter adapter;
    ListView lv;
    FirebaseDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klausurtermine_prof);
        ref = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        add_kl = findViewById(R.id.add_kl);
        back_kl = findViewById(R.id.back_kl);
        add_kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KlausurAddProfActivity.class);
                v.getContext().startActivity(intent);
            }

        });
        back_kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomepageProfActivity.class);
                v.getContext().startActivity(intent);
            }

        });
    }
    protected void onStart() {
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String em= Prevalent.Profmail.replace('.','d');
                    Professor Data = dataSnapshot.child("Professor").child(em).getValue(Professor.class);
                    String Unterrictl = Data.getUnterricht();
                    List<String> Mkl = Arrays.asList((Unterrictl.split(",")));
                    list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.child("Klausur").getChildren()) {
                        Klausur data = ds.getValue(Klausur.class);

                            if (Mkl.contains(data.getKlausurId())) {
                                if(data.getPruefer().equals(" ")||data.getDatum().equals(" ")||data.getStartzeit().equals(" ")||data.getRaum().equals(" ")) {
                                }
                                else {
                                    list.add(ds.getValue(Klausur.class));
                                }
                            }

                    }
                    KlausurtermineAdapter adapterClass = new KlausurtermineAdapter(list);

                    recyclerView.setAdapter(adapterClass);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(KlausurtermineProf.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

    }
    private void search(String str){
        ArrayList<Klausur> myList = new ArrayList<>();
        for(Klausur object:list){
            if(object.getFach().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        KlausurListAdapter adapterClass = new KlausurListAdapter(myList);
        recyclerView.setAdapter(adapterClass);
    }
}



