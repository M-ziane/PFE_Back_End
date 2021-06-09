package com.pfe.project.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CC {
    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ax;
    private String nom;
    private String succ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
/*
private int numEquipe;
public int getNumEquipe(){
    return numEquipe;
}
public int setNumEquipe(int numEquipe){
    this.numEquipe = numEquipe;
}
//Constructor
public CC(nom, ax,succ ,numEquipe){
    this.nom = nom;
    this.succ= succ;
    this.numEquipe = numEquipe;
    this.ax = ax;
}
 */
    public String getAx() {
        return ax;
    }

    public void setAx(String ax) {
        this.ax = ax;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSucc() {
        return succ;
    }

    public void setSucc(String succ) {
        this.succ = succ;
    }
}
