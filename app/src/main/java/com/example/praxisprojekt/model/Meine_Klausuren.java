package com.example.praxisprojekt.model;

import java.util.ArrayList;
import java.util.List;

public class Meine_Klausuren {

    List<String> MKlausur = new ArrayList<String>();

    public Meine_Klausuren(List<String> MKlausur) {
        this.MKlausur = MKlausur;
    }

    public Meine_Klausuren() {
    }

    public List<String> getMKlausur() {
        return MKlausur;
    }

    public void setMKlausur(List<String> MKlausur) {
        this.MKlausur = MKlausur;
    }


}
