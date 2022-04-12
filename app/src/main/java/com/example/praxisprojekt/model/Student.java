package com.example.praxisprojekt.model;

public class Student
{
    private Long matrikelnr;
    private String password;
    private String meineKlausuren;


    private String token;
    public Student() {
    }
    public Student(Long matrikelnr, String password, String meineKlausuren,String token) {
        this.matrikelnr = matrikelnr;
        this.password = password;
        this.meineKlausuren = meineKlausuren;
        this.token = token;
    }
    public Long getMatrikelnr() {
        return matrikelnr;
    }

    public void setMatrikelnr(Long matrikelnr) {
        this.matrikelnr = matrikelnr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMeineKlausuren() {
        return meineKlausuren;
    }

    public void setMeineKlausuren(String meineKlausuren) {
        this.meineKlausuren = meineKlausuren;
    }

}
