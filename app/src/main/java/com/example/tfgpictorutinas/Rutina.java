package com.example.tfgpictorutinas;

public class Rutina {
    long idRutina;
    String nombre;
    String foto;

    String repeticiones;

    public Rutina(long idRutina, String nombre, String foto, String repeticiones){
        this.idRutina=idRutina;
        this.nombre=nombre;
        this.foto=foto;
        this.repeticiones=repeticiones;
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

    public String getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(String repeticiones) {
        this.repeticiones = repeticiones;
    }
}
