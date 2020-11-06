package com.example.myanimelist;

public class DatosAnimesVistos {


    private String AnimeVisto;
    private String Puntuacion;
    private String EpisodioActual;
    private String EpisodioUltimo;

    public DatosAnimesVistos() {

    }


    public DatosAnimesVistos(String AV, String P, String A, String U) {

        AnimeVisto = AV;
        Puntuacion = P;
        EpisodioActual = A;
        EpisodioUltimo = U;

    }

    public String getAnimeVisto() {
        return AnimeVisto;
    }

    public void setAnimeVisto(String AV) {
        AnimeVisto = AV;
    }

    public String getPuntuacion() {
        return Puntuacion;
    }

    public void setPuntuacion(String P) {
        Puntuacion = P;
    }

    public String getEpisodioActual() {
        return EpisodioActual;
    }

    public void setEpisodioActual(String A) {
        EpisodioActual = A;
    }

    public String getEpisodioUltimo() {
        return EpisodioUltimo;
    }

    public void setEpisodioUltimo(String U) {
        EpisodioUltimo = U;
    }

}
