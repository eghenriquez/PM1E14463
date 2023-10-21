package com.example.pm1e13044.Class;

public class Paises {

    private String codigo;
    private String nombrePais;

    public Paises() {

    }

    public Paises(String codigo, String nombrePais) {
        this.codigo = codigo;
        this.nombrePais = nombrePais;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

}
