package com.example.myanimelist;

public class DatosAnimesPendientes {

    private String AnimePendiente;
    private String EpisodioActual;
    private String Temporada;
    private String Año;

    public DatosAnimesPendientes() {

    }


    public DatosAnimesPendientes(String AP, String A, String T, String Y) {

        AnimePendiente = AP;
        EpisodioActual = A;
        Temporada = T;
        Año = Y;

    }

    public String getAnimePendiente() {
        return AnimePendiente;
    }

    public void setAnimePendiente(String AP) {
        AnimePendiente = AP;
    }

    public String getEpisodioActual() {
        return EpisodioActual;
    }

    public void setEpisodioActual(String A) {
        EpisodioActual = A;
    }

    public String getTemporada() {
        return Temporada;
    }

    public void setTemporada(String T) {
        Temporada = T;
    }

    public String getAño() {
        return Año;
    }

    public void setAño(String Y) {
        Año = Y;
    }


}
