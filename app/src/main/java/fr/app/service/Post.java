package fr.app.service;

public class Post {
    private String id;
    private String nom;
    private String periode_ouverture;
    private String adresse;
    private String ville;
    private String ferme;
    private String fermeture_annuelle;
    private String site_web;
    private int cp;
    private String region;
    private String dept;

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPeriode_ouverture() {
        return periode_ouverture;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getFerme() {
        return ferme;
    }

    public String getFermeture_annuelle() {
        return fermeture_annuelle;
    }

    public String getSite_web() {
        return site_web;
    }

    public int getCp() {
        return cp;
    }

    public String getRegion() {
        return region;
    }

    public String getDept() {
        return dept;
    }
}
