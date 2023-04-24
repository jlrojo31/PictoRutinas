package com.example.tfgpictorutinas.firebaseRDB;

public class UsuariosRutinas {

    String nombreUsuario;
    long idRutina;

    String idNombre;

    public UsuariosRutinas(String nombreUsuario, long idRutina) {
        this.nombreUsuario = nombreUsuario;
        this.idRutina = idRutina;
        this.idNombre = nombreUsuario+ String.valueOf(idRutina);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public long getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(long idRutina) {
        this.idRutina = idRutina;
    }

    public String getIdNombre() {
        return idNombre;
    }

    public void setIdNombre(String idNombre) {
        this.idNombre = idNombre;
    }
}
