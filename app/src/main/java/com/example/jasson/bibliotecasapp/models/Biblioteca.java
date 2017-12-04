package com.example.jasson.bibliotecasapp.models;

/**
 * Created by jasson on 10/15/17.
 */

public class Biblioteca {


    private String barrio;
    private String centro_poblado;
    private String departamento;
    private String direcci_n_de_la_biblioteca;
    private String georeferencia;
    private String municipio;
    private String naturaleza_de_la_biblioteca;
    private String nombre_de_la_biblioteca;
    private String tel_fonos_de_contacto;
    private String tipo_de_biblioteca;
    private String latitud;
    private String longitud;


    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCentro_poblado() {
        return centro_poblado;
    }

    public void setCentro_poblado(String centro_poblado) {
        this.centro_poblado = centro_poblado;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDirecci_n_de_la_biblioteca() {
        return direcci_n_de_la_biblioteca;
    }

    public void setDirecci_n_de_la_biblioteca(String direcci_n_de_la_biblioteca) {
        this.direcci_n_de_la_biblioteca = direcci_n_de_la_biblioteca;
    }

    public String getGeoreferencia() {
        return georeferencia;
    }

    public void setGeoreferencia(String georeferencia) {
        this.georeferencia = georeferencia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNaturaleza_de_la_biblioteca() {
        return naturaleza_de_la_biblioteca;
    }

    public void setNaturaleza_de_la_biblioteca(String naturaleza_de_la_biblioteca) {
        this.naturaleza_de_la_biblioteca = naturaleza_de_la_biblioteca;
    }

    public String getNombre_de_la_biblioteca() {
        return nombre_de_la_biblioteca;
    }

    public void setNombre_de_la_biblioteca(String nombre_de_la_biblioteca) {
        this.nombre_de_la_biblioteca = nombre_de_la_biblioteca;
    }

    public String getTel_fonos_de_contacto() {
        return tel_fonos_de_contacto;
    }

    public void setTel_fonos_de_contacto(String tel_fonos_de_contacto) {
        this.tel_fonos_de_contacto = tel_fonos_de_contacto;
    }

    public String getTipo_de_biblioteca() {
        return tipo_de_biblioteca;
    }

    public void setTipo_de_biblioteca(String tipo_de_biblioteca) {
        this.tipo_de_biblioteca = tipo_de_biblioteca;
    }

    @Override
    public String toString() {
        return
                "Nombre: " + nombre_de_la_biblioteca +
                "\nDireccion: " + direcci_n_de_la_biblioteca +
                "\nBarrio: " + barrio +
                "\nMunicipio: " + municipio +
                "\nDepartamento: " + departamento +
                "\nNaturaleza: " + naturaleza_de_la_biblioteca +
                "\nContactos: " + tel_fonos_de_contacto.replace("/"," | ") +
                "\nTipo: " + tipo_de_biblioteca;
    }

    public String nombrebiblioteca(){
        return nombre_de_la_biblioteca;
    }

    public String getLatitud() {
        String[] str = georeferencia.split(",");
        String str2 = str[0].replace("(","");
        return str2.replace("°","");
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        String[] str = georeferencia.split(",");
        String str2 = str[1].replace(")","");
        return str2.replace("°","");
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
