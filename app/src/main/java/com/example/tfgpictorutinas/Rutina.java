package com.example.tfgpictorutinas;

public class Rutina {
    long idRutina;
    String nombre;
    String foto;

    public Rutina(long idRutina, String nombre, String foto){
        this.idRutina=idRutina;
        this.nombre=nombre;
        this.foto=foto;
    }

    public long getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(long idRutina) {
        this.idRutina = idRutina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
