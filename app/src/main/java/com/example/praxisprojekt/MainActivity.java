package com.example.praxisprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praxisprojekt.professor.ProfessorLoginActivity;
import com.example.praxisprojekt.student.StudentLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private ImageView student_image_login, professor_image_login;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if(task.isSuccessful()){
                            String token=task.getResult().getToken();
                           // Log.d(TAG, "onComplete: Token: "+token);
                        }
                    }
                });

        student_image_login = (ImageView) findViewById(R.id.img_btn_student_login);
        student_image_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }
        });

        professor_image_login = (ImageView) findViewById(R.id.img_btn_professor_login);
        professor_image_login.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, ProfessorLoginActivity.class);
        startActivity(intent);
    }
    });
}

}
