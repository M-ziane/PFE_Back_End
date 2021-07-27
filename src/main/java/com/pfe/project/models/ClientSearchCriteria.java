package com.pfe.project.models;

import java.util.Date;
import java.util.List;

public class ClientSearchCriteria {
    private String name;
    private String sexe;
    private String typologie;
    private String marque;
    private String modele;
    private String startDate ;
    private String endDate ;
    private  Boolean mdt;
    private String ptVente;
    private  String Commerciale ;
    private int slm;
    private String user;
    private Long kilometrage;
    private Long kilometrage2;

    public Long getKilometrage2() {
        return kilometrage2;
    }

    public void setKilometrage2(Long kilometrage2) {
        this.kilometrage2 = kilometrage2;
    }

    public Long getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(Long kilometrage) {
        this.kilometrage = kilometrage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getstartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public int getSlm() {
        return slm;
    }

    public void setSlm(int slm) {
        this.slm = slm;
    }

    public Boolean isMdt() {
        return mdt;
    }

    public void setMdt(Boolean mdt) {
        this.mdt = mdt;
    }

    public String getPtVente() {
        return ptVente;
    }

    public void setPtVente(String ptVente) {
        this.ptVente = ptVente;
    }

    public String getCommerciale() {
        return Commerciale;
    }

    public void setCommerciale(String commerciale) {
        Commerciale = commerciale;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getName() {
        return name;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypologie() {
        return typologie;
    }

    public void setTypologie(String typologie) {
        this.typologie = typologie;
    }
}