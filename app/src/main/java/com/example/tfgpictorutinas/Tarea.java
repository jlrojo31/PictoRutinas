package com.example.tfgpictorutinas;

public class Tarea {
    long idTarea;
    String nombreTarea;
    String fotoTarea;
    String hora_ini;
    String hora_end;
    String rutina_id;

    public Tarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public Tarea(long idTarea, String nombreTarea, String fotoTarea,String hora_ini, String hora_end, String rutina_id) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.fotoTarea = fotoTarea;
        this.hora_ini = hora_ini;
        this.hora_end = hora_end;
        this.rutina_id = rutina_id;
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
