package com.example.a2dam.actividad5a.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Matias on 04/01/2018.
 */

public class Producto implements Parcelable {

    private String nombre;
    private String descripcion;
    private String categoria;
    private String precio;
    private String uid;
    private String usuario;
    private String key;

    public Producto() {

    }

    public Producto(String nombre, String descripcion, String categoria, String precio, String uid, String usuario, String key) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.uid = uid;
        this.usuario = usuario;
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getUsuario() {return usuario; }

    public void setUsuario() { this.usuario = usuario; }

    public String getKey() { return  key; }

    public void setKey() { this.key = key; }

    protected Producto(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        categoria = in.readString();
        precio = in.readString();
        uid = in.readString();
        usuario = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(categoria);
        dest.writeString(precio);
        dest.writeString(uid);
        dest.writeString(usuario);
        dest.writeString(key);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Producto> CREATOR = new Parcelable.Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };
}
