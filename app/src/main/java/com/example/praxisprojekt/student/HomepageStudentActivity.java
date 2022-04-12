package com.example.praxisprojekt.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.praxisprojekt.FCMSend;
import com.example.praxisprojekt.MainActivity;
import com.example.praxisprojekt.student.Quiz.QuizFachActivity;
import com.example.praxisprojekt.R;

public class HomepageStudentActivity extends AppCompatActivity {

    private CardView klausur_image, ergebnis_image,meineKlausuren,Quiz,logoff;

    private static String serverKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // FCMSend Initialization
        FCMSend.SetServerKey(serverKey);
        klausur_image = (CardView) findViewById(R.id.img_btn_klausur);
        klausur_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageStudentActivity.this, KlausurListActivity.class);
                startActivity(intent);
            }
        });

        ergebnis_image = (CardView) findViewById(R.id.img_btn_upload);
        ergebnis_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KlausurErgebnis.class);
                startActivity(intent);
            }
        });
        meineKlausuren = (CardView) findViewById(R.id.meine_klausur);
        meineKlausuren.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeineKlausurenActivity.class);
                startActivity(intent);
            }
        });
        Quiz = (CardView) findViewById(R.id.img_btn_logoff1);
        Quiz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizFachActivity.class);
                startActivity(intent);
            }
        });
        logoff = (CardView) findViewById(R.id.img_btn_logoff);
        logoff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
