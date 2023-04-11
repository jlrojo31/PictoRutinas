package com.example.tfgpictorutinas;

public class Tarea {
    long idTarea;
    String nombreTarea;
    String fotoTarea;

    public Tarea(long idTarea, String nombreTarea, String fotoTarea) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.fotoTarea = fotoTarea;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getFotoTarea() {
        return fotoTarea;
    }

    public void setFotoTarea(String fotoTarea) {
        this.fotoTarea = fotoTarea;
    }
}
