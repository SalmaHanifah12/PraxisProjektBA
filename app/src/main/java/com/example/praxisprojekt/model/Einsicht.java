package com.example.praxisprojekt.model;

public class Einsicht {
    private String KlausurId;
    private String fach;
    private String Zeit;
    private String raum;
    private String startzeit;

    public String getStartzeit() {
        return startzeit;
    }

    public void setStartzeit(String startzeit) {
        this.startzeit = startzeit;
    }

    public Einsicht(String KlausurId, String fach,String Zeit, String raum, String startzeit) {
        this.KlausurId = KlausurId;
        this.fach = fach;
        this.Zeit = Zeit;
        this.raum = raum;
        this.startzeit = startzeit;

    }

    public Einsicht() {
    }

    public String getKlausurId() {
        return KlausurId;
    }

    public void setKlausurId(String klausurId) {
        KlausurId = klausurId;
    }
    public String getFach(){return fach;}
    public void setFach(String Fach){
        fach = Fach;
    }

    public String getZeit() {
        return Zeit;
    }

    public void setZeit(String zeit) {
        Zeit = zeit;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }
}

