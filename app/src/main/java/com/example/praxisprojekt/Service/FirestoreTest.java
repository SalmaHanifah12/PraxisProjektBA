package com.example.praxisprojekt.Service;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praxisprojekt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreTest extends AppCompatActivity {

    private EditText InputMatrikelnummer, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String token;
    private String parentDbName = "Student";
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestoretest);

        LoginButton = (Button) findViewById(R.id.login_btn_student);
        InputPassword = (EditText) findViewById(R.id.login_student_pass_input);
        InputMatrikelnummer = (EditText) findViewById(R.id.login_matrikelnummer_input);
        loadingBar = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matrikelnr = InputMatrikelnummer.getText().toString();
                String password = InputPassword.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put("matrikelnum", matrikelnr);
                user.put("password", password);
                db.collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FirestoreTest.this,"Success",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }


}
