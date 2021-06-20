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
    private Long id;

    private String ax;
    private String nom;
    private String succ;
    private int numEquipe ;
    private Boolean chef;
public CC(){

}
    public CC(String nom, String ax, String succ, int numEquipe, Boolean chef) {
        this.nom = nom;
        this.succ= succ;
        this.numEquipe = numEquipe;
        this.ax = ax;
        this.chef = chef;
    }


    public Boolean getChef() {
        return chef;
    }

    public void setChef(Boolean chef) {
        this.chef = chef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumEquipe(){
        return numEquipe;
    }
    public void setNumEquipe(int numEquipe) {
        this.numEquipe = numEquipe;
    }

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
