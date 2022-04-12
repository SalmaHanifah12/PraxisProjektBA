package com.example.praxisprojekt.model;

public class Klausur {



    private String KlausurId;
    private String datum;
    private String fach;
    private String pruefer;
    private String raum;
    private String startzeit;

    public Klausur(String KlausurId, String datum, String fach, String pruefer, String raum, String startzeit) {
        this.KlausurId = KlausurId;
        this.datum = datum;
        this.fach = fach;
        this.pruefer = pruefer;
        this.raum = raum;
        this.startzeit = startzeit;

    }

    public Klausur() {
    }


    public String getKlausurId() {
        return KlausurId;
    }

    public void setKlausurId(String klausurId) {
        KlausurId = klausurId;
    }


    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getPruefer() {
        return pruefer;
    }

    public void setPruefer(String pruefer) {
        this.pruefer = pruefer;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public String getStartzeit() {
        return startzeit;
    }

    public void setStartzeit(String startzeit) {
        this.startzeit = startzeit;
    }



}
