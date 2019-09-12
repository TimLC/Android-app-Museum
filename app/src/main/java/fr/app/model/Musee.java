package fr.app.model;

import java.io.Serializable;

public class Musee implements Serializable {

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

    public Musee(String id, String nom, String periode_ouverture, String adresse, String ville, String ferme, String fermeture_annuelle, String site_web, int cp, String region, String dept) {
        this.id = id;
        this.nom = nom;
        this.periode_ouverture = periode_ouverture;
        this.adresse = adresse;
        this.ville = ville;
        this.ferme = ferme;
        this.fermeture_annuelle = fermeture_annuelle;
        this.site_web = site_web;
        this.cp = cp;
        this.region = region;
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "Musee{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", periode_ouverture='" + periode_ouverture + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", ferme='" + ferme + '\'' +
                ", fermeture_annuelle='" + fermeture_annuelle + '\'' +
                ", site_web='" + site_web + '\'' +
                ", cp=" + cp +
                ", region='" + region + '\'' +
                ", dept='" + dept + '\'' +
                '}';
    }

    public Musee() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPeriode_ouverture(String periode_ouverture) {
        this.periode_ouverture = periode_ouverture;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setFerme(String ferme) {
        this.ferme = ferme;
    }

    public void setFermeture_annuelle(String fermeture_annuelle) {
        this.fermeture_annuelle = fermeture_annuelle;
    }

    public void setSite_web(String site_web) {
        this.site_web = site_web;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

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
