package com.example.a2dam.actividad5a.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Matias on 18/12/2017.
 */
public class Usuario implements Parcelable {

    private String key;
    private String usuario;
    private String correo;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String uid;


    public Usuario(){

    }

    public Usuario(String usuario, String correo, String nombre, String apellidos, String direccion, String uid, String key) {
        this.usuario = usuario;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.uid = uid;
        this.key = key;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    protected Usuario(Parcel in) {
        usuario = in.readString();
        correo = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        direccion = in.readString();
        uid = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usuario);
        dest.writeString(correo);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(direccion);
        dest.writeString(uid);
        dest.writeString(key);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}