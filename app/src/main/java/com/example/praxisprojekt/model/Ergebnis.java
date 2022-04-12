package com.example.praxisprojekt.model;

public class Ergebnis {
    public String name;
    public String url;
    public String id;
    public String kid;
    public Ergebnis() {
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;

    } public String getKid() {
        return kid;
    }
    public void setKid(String Kid) {
        this.kid = Kid;
    }
    public Ergebnis(String name, String url,String id,String kid) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.kid = kid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
