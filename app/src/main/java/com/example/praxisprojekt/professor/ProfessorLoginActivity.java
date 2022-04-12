package com.example.praxisprojekt.professor;
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
import com.example.praxisprojekt.model.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class ProfessorLoginActivity extends AppCompatActivity {
    private EditText InputEmail, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String token;
    private String parentDbName = "Professor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_login);
        LoginButton = (Button) findViewById(R.id.login_btn_student);
        InputPassword = (EditText) findViewById(R.id.login_professor_pass_input);
        InputEmail = (EditText) findViewById(R.id.login_email_input);
        loadingBar = new ProgressDialog(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            token=task.getResult().getToken();

                        }
                    }
                });
        Prevalent.ProfToken = token;
            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginProfessor();
                }
            });

    }



        private void LoginProfessor()
        {
            String email = InputEmail.getText().toString();
            String password = InputPassword.getText().toString();

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "Bitte geben Sie ihre Emailadresse ein", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loadingBar.setTitle("Login");
                loadingBar.setMessage("Bitte waten Sie.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                //Long mat = Long.parseLong(matrikelnr);
                AllowAccessToAccount(email, password);
            }

        }



        private void AllowAccessToAccount(String email, final String password)
        {

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

                final String eMail = email;
                        // .substring(0, email.length() - 4);
                final String em= email.replace('.','d');

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.child(parentDbName).child(em).exists())
                    {
                        final Professor usersData = dataSnapshot.child("Professor").child(em).getValue(Professor.class);

                        if (usersData.geteMail().equals(eMail))
                        {

                            if (usersData.getPassword().equals(password))
                            {
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(eMail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override

                                    public void onSuccess(AuthResult authResult) {
                                     //   Toast.makeText(ProfessorLoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                loadingBar.dismiss();
                                Prevalent.Profmail=usersData.geteMail();
                                Prevalent.ProfPasswordKey=usersData.getPassword();
                                //Toast.makeText(ProfessorLoginActivity.this, Prevalent.Profmail, Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(ProfessorLoginActivity.this, HomepageProfActivity.class);
                                startActivity(intent2);
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(ProfessorLoginActivity.this, "Falsches Passwort.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(ProfessorLoginActivity.this, "Professor mit dieser ´" + eMail + "´ E-Mail-Adresse existiert nicht.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

}
