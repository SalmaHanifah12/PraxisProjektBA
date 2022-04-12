package com.example.praxisprojekt.student;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praxisprojekt.Service.FirebaseDatabaseHelper;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Klausur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KlausurListActivity extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Klausur> list;
    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseDatabaseHelper helper;
    KlausurListAdapter adapter;
    ListView lv;
    FirebaseDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klausur_uebersicht);
        ref = FirebaseDatabase.getInstance().getReference().child("Klausur");
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
    }
    protected void onStart() {
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Klausur data = ds.getValue(Klausur.class);
                            if(data.getPruefer().equals(" ")||data.getDatum().equals(" ")) {
                            }
                            else {
                                list.add(ds.getValue(Klausur.class));
                            }
                            }

                        KlausurListAdapter adapterClass = new KlausurListAdapter(list);
                        recyclerView.setAdapter(adapterClass);
                        searchView = findViewById(R.id.searchView);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(KlausurListActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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