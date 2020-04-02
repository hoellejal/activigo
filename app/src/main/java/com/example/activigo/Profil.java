package com.example.activigo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe gérant les profils
 */
public final class Profil implements Serializable {

    //id du profil
    private String idProfil;

    //wishlist: list d'id d'activités
    private ArrayList<String> wishlist;

    //Infos des seekbars
    private int nature;
    private int urbain;
    private int populaire;
    private int peu_connu;
    private int budget;
    private int culture;
    private int groupe;
    private int divertissement;
    private int gastronomie;
    private int sport;

    /**
     * Constructeur d'un profil
     * @param idProfil l'id généré automatiquement par firestore
     * @param wishlist une liste d'id d'activité
     * @param nature int représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param urbain int représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param populaire int représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param peu_connu int représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param budget int représentant le budget de l'utilisateur
     * @param culture booléen représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param groupe booléen représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param divertissement booléen représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param gastronomie booléen représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     * @param sport booléen représentant à quel point l'utilisateur est intéressé dans ce type d'activité
     */
    public Profil(String idProfil, ArrayList<String> wishlist, int nature, int urbain, int populaire, int peu_connu, int budget, int culture, int groupe, int divertissement, int gastronomie, int sport) {
        this.idProfil=idProfil;
        this.wishlist=wishlist;
        this.nature = nature;
        this.urbain = urbain;
        this.populaire = populaire;
        this.peu_connu=peu_connu;
        this.budget=budget;
        this.culture = culture;
        this.groupe = groupe;
        this.gastronomie = gastronomie;
        this.divertissement = divertissement;
        this.sport = sport;
    }

    public Profil() {

    }

    /* getters et setters pour tous les champs */

    public String getIdProfil(){ return idProfil; }

    void setIdProfil(String idProfil) {
        this.idProfil = idProfil;
    }

    public ArrayList<String> getWishlist(){ return wishlist;}

    public void setWishlist(ArrayList<String> wishlist){ this.wishlist=wishlist;}

    public void addTowishlist(String id){wishlist.add(id);}

    public void removeFromWishlist(String id){wishlist.remove(id);}

    public int getNature() {
        return nature;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }

    public int getUrbain() {
        return urbain;
    }

    public void setUrbain(int urbain) {
        this.urbain = urbain;
    }

    public int getPopulaire() {
        return populaire;
    }

    public void setPopulaire(int populaire) {
        this.populaire = populaire;
    }

    public int getPeu_connu() {
        return peu_connu;
    }

    public void setPeu_connu(int peu_connu) {
        this.peu_connu = peu_connu;
    }

    public int getBudget() { return budget; }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getCulture() {
        return culture;
    }

    public void setCulture(int culture) {
        this.culture = culture;
    }

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public int getGastronomie() {
        return gastronomie;
    }

    public void setGastronomie(int gastronomie) {
        this.gastronomie = gastronomie;
    }

    public int getDivertissement() {
        return divertissement;
    }

    public void setDivertissement(int divertissement) {
        this.divertissement = divertissement;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }
}
