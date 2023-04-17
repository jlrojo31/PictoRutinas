package com.example.tfgpictorutinas.firebaseRDB;

public class Usuario {

    String nombre ="";
    String email="";
    boolean administrador=false;

    public Usuario(String nombre, String email, boolean administrador) {
        this.nombre = nombre;
        this.email = email;
        this.administrador = administrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
}
