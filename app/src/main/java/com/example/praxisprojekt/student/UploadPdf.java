package com.example.praxisprojekt.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praxisprojekt.FCMSend;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.SendNotificationPack.APIService;
import com.example.praxisprojekt.SendNotificationPack.Client;
import com.example.praxisprojekt.model.Ergebnis;
import com.example.praxisprojekt.model.Klausur;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
public class UploadPdf extends AppCompatActivity {

    EditText editPDFName;
    EditText editId;
    Button btn_upload;
    private APIService apiService;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference studentReference;
    FirebaseDatabase database;
    private String test;
    ArrayList<Klausur> list;
    DatabaseReference RootRef1;
    ArrayList<Klausur> list1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        editPDFName = findViewById(R.id.txt_pdfName);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        editId = findViewById(R.id.txt_id);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        studentReference = FirebaseDatabase.getInstance().getReference("Student");

        apiService =
                Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
//Initialize the object of APIService with client class
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance().getReference().child("Ergebnis").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // do your stuff
                    selectPDFFile();
                } else {
                    signInAnonymously();
                }
            }
        });
    }

    private void selectPDFFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uploadPDFFile(data.getData());
        }
    }


    private void uploadPDFFile(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();
        StorageReference reference = storageReference.child("Ergebnis/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                Uri url = uri.getResult();
                RootRef1 = FirebaseDatabase.getInstance().getReference().child("Klausur");
                RootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list1 = new ArrayList<>();

                        for(DataSnapshot ds:dataSnapshot.getChildren()) {
                            Klausur klausur = ds.getValue(Klausur.class);
                            String fach1 = klausur.getFach();
                            if(fach1.equals(editId.getText().toString())){
                                Ergebnis Ergebnis = new Ergebnis(editPDFName.getText().toString(),url.toString(),editId.getText().toString(),klausur.getKlausurId());
                                databaseReference.child("Ergebnis").child(databaseReference.push().getKey()).setValue(Ergebnis);
                                progressDialog.dismiss();
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int)progress+"%");
            }
        });
       HashMap<String, String> data1 = new HashMap<>();
        data1.put("key1", "data 1");
        data1.put("key2", "data 2");
        data1.put("key3", "data 3");

        FCMSend.Builder build = new FCMSend.Builder(editId.getText().toString(), true)
                .setTitle(editId.getText().toString()+"-Ergebnis")
                .setBody("Das Ergebis von "+editId.getText().toString()+ " wurde hochgeladen.")
                .setClickAction("<Action>") // Optional
                .setData(data1); // Optional
        build.send();

    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                selectPDFFile();
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }
}
