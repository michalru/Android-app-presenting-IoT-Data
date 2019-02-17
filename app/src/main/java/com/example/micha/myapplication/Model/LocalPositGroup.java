package com.example.micha.myapplication.Model;
//Klasa na lokalizacje i pozycje sensora
public class LocalPositGroup {
    private String name;
    private int idp;

    public LocalPositGroup(String name, int idp) {
        this.name = name;
        this.idp = idp;
    }

    @Override
    public String toString() {
        return "LocalPositGroup{" +
                "name='" + name + '\'' +
                ", idp=" + idp +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getIdp() {
        return idp;
    }
}
