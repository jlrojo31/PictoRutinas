package com.example.tfgpictorutinas.firebaseRDB;

public class RutinasTareas {

    long idRutina;
    long idTarea;
    long orden;

    public RutinasTareas(long idRutina, long idTarea, long orden) {
        this.idRutina = idRutina;
        this.idTarea = idTarea;
        this.orden = orden;
    }

    public long getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(long idRutina) {
        this.idRutina = idRutina;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }
}
