package com.example.praxisprojekt.Service;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.praxisprojekt.model.Klausur;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceKlausuren;
    private List<Klausur> klausuren = new ArrayList<>();

    public interface  DataStatus{
        void DataIsLoaded(List<Klausur> klausuren, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(DatabaseReference db){
       mDatabase = FirebaseDatabase.getInstance();
       mReferenceKlausuren = mDatabase.getReference("klausuren");
    }
    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    public void fetch(final DataStatus dataStatus){
        mReferenceKlausuren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                klausuren.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Klausur klausur = keyNode.getValue(Klausur.class);
                    klausuren.add(klausur);
                }
                dataStatus.DataIsLoaded(klausuren, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
