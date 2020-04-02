package com.example.activigo;

import java.io.Serializable;

public final class Activite implements Serializable {
    private String nomActivite;
    private String idActivite;
    private String photoActivite;
    private String description;
    private float lngActivite;
    private float lattActivite;
    private float noteActivite;
    private int nbnote;

    // Infos de la Seekbar
    private int popularite;
    private int prix;
    private int nature;


    // Infos des Checkbox
    private boolean culture;
    private boolean groupe;
    private boolean divertissement;
    private boolean endroits;
    private boolean gastronomie;
    private boolean sport;

    public Activite(){}

    /**
     * Constructeur pour une activité
     * @param idActivite id généré automatiquement par firebase
     * @param nomActivite nom de l'activité
     * @param photoActivite uri de téléchargement de la photo depuis firebase storage
     * @param description description de l'activité
     * @param lngActivite longitude de l'activité
     * @param lattActivite lattitude de l'activité
     * @param noteActivite note de l'activité
     * @param popularite niveau de popularité de l'activité
     * @param prix niveau moyen du prix de l'activité
     * @param nature l'activité est elle dans la nature ou dans une ville
     * @param culture l'activité est elle culturelle
     * @param groupe l'activité est elle faisable en groupe
     * @param divertissement l'activité est elle divertissante
     * @param endroits l'activité est elle dans un point de vue
     * @param gastronomie l'activité est elle gastronomique
     * @param sport l'activité est elle sportive
     * @param nbnote combien de notes ont été attribuées
     */
    public Activite(String idActivite, String nomActivite, String photoActivite, String description, float lattActivite, float lngActivite,float noteActivite, int popularite, int prix, int nature, boolean culture, boolean groupe, boolean divertissement, boolean endroits, boolean gastronomie, boolean sport, int nbnote) {
        this.idActivite=idActivite;
        this.nomActivite = nomActivite;
        this.photoActivite = photoActivite;
        this.description = description;
        this.lngActivite = lngActivite;
        this.lattActivite = lattActivite;
        this.noteActivite = noteActivite;
        this.nbnote=nbnote;
        this.popularite = popularite;
        this.prix = prix;
        this.nature = nature;
        this.culture = culture;
        this.groupe = groupe;
        this.divertissement = divertissement;
        this.endroits = endroits;
        this.gastronomie = gastronomie;
        this.sport = sport;
    }

    /* getters et setters pour tous les champs */

    public String getIdActivite(){ return idActivite; }

    public void setIdActivite(String idActivite) {
        this.idActivite = idActivite;
    }

    public String getNomActivite() {
        return nomActivite;
    }

    public void setNomActivite(String nomActivite) {
        this.nomActivite = nomActivite;
    }

    public String getPhotoActivite() {
        return photoActivite;
    }

    public void setPhotoActivite(String photoActivite) {
        this.photoActivite = photoActivite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLngActivite() {
        return lngActivite;
    }

    public void setLngActivite(float lngActivite) {
        this.lngActivite = lngActivite;
    }

    public float getLattActivite() {
        return lattActivite;
    }

    public void setLattActivite(float lattActivite) {
        this.lattActivite = lattActivite;
    }
    public float getNoteActivite() {
        return noteActivite;
    }

    public void setNoteActivite(float noteActivite) {
        this.noteActivite = noteActivite;
    }

    public float changeNote(float note){this.noteActivite=(this.noteActivite*nbnote+note)/++nbnote; return noteActivite;}

    public int getNbnote(){ return nbnote;}

    public void setNbnote(int nbnote){this.nbnote=nbnote;}

    public int getPopularite() {
        return popularite;
    }

    public void setPopularite(int popularite) {
        this.popularite = popularite;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getNature() {
        return nature;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }

    public boolean isCulture() {
        return culture;
    }

    public void setCulture(boolean culture) {
        this.culture = culture;
    }

    public boolean isGroupe() {
        return groupe;
    }

    public void setGroupe(boolean groupe) {
        this.groupe = groupe;
    }

    public boolean isDivertissement() {
        return divertissement;
    }

    public void setDivertissement(boolean divertissement) {
        this.divertissement = divertissement;
    }

    public boolean isEndroits() {
        return endroits;
    }

    public void setEndroits(boolean endroits) {
        this.endroits = endroits;
    }

    public boolean isGastronomie() {
        return gastronomie;
    }

    public void setGastronomie(boolean gastronomie) {
        this.gastronomie = gastronomie;
    }

    public boolean isSport() {
        return sport;
    }

    public void setSport(boolean sport) {
        this.sport = sport;
    }


}
