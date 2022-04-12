package com.example.praxisprojekt.professor.klausureinsicht;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.model.Einsicht;
import com.example.praxisprojekt.Service.FirebaseDatabaseHelper;
import com.example.praxisprojekt.student.KlausurListAdapter;
import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Professor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EinsichtAddProfActivity extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Einsicht> list;
    RecyclerView recyclerView;
    SearchView searchView;
    Button add_kl;
    ImageView back_kl;
    FirebaseDatabaseHelper helper;
    KlausurListAdapter adapter;
    ListView lv;
    FirebaseDatabase mDatabase;
    private ImageView Add_btn;
    private TextView datum,  fach, pruefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klausur_add_prof);
        ref = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        Add_btn = (ImageView) findViewById(R.id.img_btn_klausur);
        fach = (TextView) findViewById(R.id.fach);
        datum = (TextView) findViewById(R.id.datum);
        pruefer = (TextView) findViewById(R.id.pruefer);
        add_kl = findViewById(R.id.add_kl);
        back_kl = findViewById(R.id.back_kl);


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
                    for (DataSnapshot ds : dataSnapshot.child("Einsicht").getChildren()) {
                        Einsicht data = ds.getValue(Einsicht.class);
                        if (Mkl.contains(data.getKlausurId())) {
                            if(data.getFach().equals(" ")||data.getZeit().equals(" ")||data.getStartzeit().equals(" ")||data.getRaum().equals(" ")) {
                                list.add(ds.getValue(Einsicht.class));}
                            else {

                            }
                        }

                    }
                    EinsichtAddProfAdapter adapterClass = new EinsichtAddProfAdapter(list);

                    recyclerView.setAdapter(adapterClass);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
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
        ArrayList<Einsicht> myList = new ArrayList<>();
        for(Einsicht object:list){
            if(object.getZeit().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        KlausurEinsichtAdapter adapterClass = new KlausurEinsichtAdapter(myList);
        recyclerView.setAdapter(adapterClass);
    }

}