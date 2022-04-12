package com.example.praxisprojekt.model;

public class Professor {

    public Professor() {
    }

    private String eMail;
    private String Unterricht;
    private String token;
    public String geteMail() {
        return eMail;
    }
    public String getUnterricht() {
        return Unterricht;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public void setUnterricht(String eMail) {
        this.Unterricht = Unterricht;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Professor(String eMail, String password,String Unterricht) {
        this.eMail = eMail;
        this.password = password;
        this.Unterricht = Unterricht;
    }

    private String password;


}
