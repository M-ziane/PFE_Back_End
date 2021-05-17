package com.pfe.project.models;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Voiture{
    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;
    private String modele;
    private String immatriculation;
    //private long km;

@OneToMany(mappedBy ="voitureA")
Set<Contrat> contrat;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /*public Long getKm() {
        return km;
    }

    public void setKm(Long km) {
        this.km = km;
    }*/

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
}