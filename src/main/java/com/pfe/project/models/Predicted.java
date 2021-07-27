package com.pfe.project.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "predicted")
public class Predicted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nom")
    private String nom;

    @Column(name="succersale")
    private String succersale;


    @Column (name="date_comptabilisation")
    private java.util.Date dateComptabilisation;

    @Column(name="typologie")
    private String typologie ;

    @Column(name="vendeur")
    private String vendeur;

    @Column(name="marque")
    private String marque;

    @Column(name="modele")
    private String modele;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSuccersale() {
        return succersale;
    }

    public void setSuccersale(String succersale) {
        this.succersale = succersale;
    }

    public Date getDateComptabilisation() {
        return dateComptabilisation;
    }

    public void setDateComptabilisation(Date dateComptabilisation) {
        this.dateComptabilisation = dateComptabilisation;
    }

    public String getTypologie() {
        return typologie;
    }

    public void setTypologie(String typologie) {
        this.typologie = typologie;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
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
}
