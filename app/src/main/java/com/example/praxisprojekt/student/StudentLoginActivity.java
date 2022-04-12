package com.example.praxisprojekt.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praxisprojekt.Prevalent;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText InputMatrikelnummer, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String token;
    private String parentDbName = "Student";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if(task.isSuccessful()){
                            token=task.getResult().getToken();

                        }
                    }
                });
        LoginButton = (Button) findViewById(R.id.login_btn_student);
        InputPassword = (EditText) findViewById(R.id.login_student_pass_input);
        InputMatrikelnummer = (EditText) findViewById(R.id.login_matrikelnummer_input);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginStudent();
            }
        });
    }



    private void LoginStudent()
    {
        String matrikelnr = InputMatrikelnummer.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(matrikelnr))
        {
            Toast.makeText(this, "Bitte geben Sie ihre Matrikelnummer ein", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //Long mat = Long.parseLong(matrikelnr);
            AllowAccessToAccount(matrikelnr, password);
        }

    }



    private void AllowAccessToAccount(final String matrikelnr, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.child(parentDbName).child(matrikelnr).exists())
                {
                    Student usersData = dataSnapshot.child("Student").child(matrikelnr).getValue(Student.class);

                    if (usersData.getMatrikelnr().equals(Long.parseLong(matrikelnr)))
                    {

                        if (usersData.getPassword().equals(password))
                        {
                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("matrikelnr", usersData.getMatrikelnr());
                                userdataMap.put("password", usersData.getPassword());
                                userdataMap.put("meineKlausuren", usersData.getMeineKlausuren());
                                userdataMap.put("token",token);
                                RootRef.child("Student").child(matrikelnr).setValue(userdataMap);
                                Prevalent.StudentMatrikelnummer = usersData.getMatrikelnr().toString();
                                Prevalent.StudentToken = token;
                               // Toast.makeText(StudentLoginActivity.this, Prevalent.StudentToken, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent2 = new Intent(StudentLoginActivity.this, HomepageStudentActivity.class);
                                startActivity(intent2);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(StudentLoginActivity.this, "Falsches Passwort.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(StudentLoginActivity.this, "Student mit dieser " + matrikelnr + " Matrikelnummer existiert nicht.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
}
