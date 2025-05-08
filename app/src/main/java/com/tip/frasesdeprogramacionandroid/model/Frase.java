package com.tip.frasesdeprogramacionandroid.model;

public class Frase {
    private String id;
    private String q;  // quote
    private String a;  // author
    private boolean favorita;

    public String getContenido() {
        return q;
    }

    public String getAutor() {
        return a;
    }

    public void setFavorita(boolean fav){
        this.favorita = fav;
    }

    public boolean esFavorita(){
        return favorita;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
