package com.example.tfgpictorutinas;

public class Tarea {
    long idTarea;
    String nombreTarea;
    String fotoTarea;
    String hora_ini;
    String hora_end;
    long rutina_id;

    Tarea() {
    }
    public Tarea(long idTarea, String nombreTarea, String fotoTarea,String hora_ini, String hora_end, long rutina_id) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.fotoTarea = fotoTarea;
        this.hora_ini = hora_ini;
        this.hora_end = hora_end;
        this.rutina_id = rutina_id;
    }

    public String getHora_ini() {
        return hora_ini;
    }

    public void setHora_ini(String hora_ini) {
        this.hora_ini = hora_ini;
    }

    public String getHora_end() {
        return hora_end;
    }

    public void setHora_end(String hora_end) {
        this.hora_end = hora_end;
    }

    public Long getRutina_id() {
        return rutina_id;
    }

    public void setRutina_id(Long rutina_id) {
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
