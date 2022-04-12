package com.example.praxisprojekt.professor;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.praxisprojekt.FCMSend;
import com.example.praxisprojekt.professor.klausureinsicht.KlausurEinsichtActivity;
import com.example.praxisprojekt.professor.klausurtermin.KlausurtermineProf;
import com.example.praxisprojekt.MainActivity;
import com.example.praxisprojekt.R;
import com.example.praxisprojekt.student.UploadPdf;
import com.example.praxisprojekt.professor.quiz.QuizFachProfessorActivity;

public class HomepageProfActivity extends AppCompatActivity {

    private CardView klausur_image, einsicht_image, ergebnis_image, Quiz_image,Abmelden_image;
    //FCM ServerKey
    private static String serverKey = "AAAAWE9QoL8:APA91bGmwq0Y4nsFc3du5LDKBdUVISgQAQj3ghaj6Hg9wXqGreKdszNXjjUMESJcyUlSPMaY3rs6R-zYeB0X42ObF8z-M_AZTf1toatUZGxsTByOD6UuLaclCVPBZoRGPpSuayDJNW-P";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_prof);
        // FCMSend Initialization
        FCMSend.SetServerKey(serverKey);
        //Toast.makeText(this, serverKey, Toast.LENGTH_LONG).show();
        klausur_image = (CardView) findViewById(R.id.img_btn_klausur);
        klausur_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageProfActivity.this, KlausurtermineProf.class);
                startActivity(intent);
            }
        });

        Quiz_image = (CardView) findViewById(R.id.img_Quiz);
        Quiz_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizFachProfessorActivity.class);
                startActivity(intent);
            }
        });
        einsicht_image = (CardView) findViewById(R.id.klausur_einsicht);
        einsicht_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(HomepageActivity.this, BearbeitenSeite.class);
                Intent intent = new Intent(getApplicationContext(), KlausurEinsichtActivity.class);
                startActivity(intent);
            }
        });
        ergebnis_image = (CardView) findViewById(R.id.img_btn_upload);
        ergebnis_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(HomepageActivity.this,UploadPdf.class);
                Intent intent = new Intent(getApplicationContext(), UploadPdf.class);
                startActivity(intent);
            }
        });
        Abmelden_image = (CardView) findViewById(R.id.img_btn_logoff);
        Abmelden_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(HomepageActivity.this,UploadPdf.class);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
