package com.pfe.project.models;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "contrat")
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voiture_ID")
    private Voiture voitureA;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_ID")
    private Client clientA;

    //extra column
    @Column(name="modalitePaiement")
    private Boolean modalitePaiement;

    @Column(name="pointVente")
    private String pointVente;

    @Column (name="date_comptabilisation")
    private java.util.Date dateComptabilisation;
   /*@Column (name="date_comptabilisation")
    @Temporal(TemporalType.DATE)
    Date dateComptabilisation;
*/
    @Column(name="Dpt")
    private String dpt ;

    @Column(name="Nom_Vendeur")
    private String nomVendeur;

    public long getContratID() {
        return id;
    }

    public Voiture getVoiture() {
        return voitureA;
    }

    public void setVoiture(Voiture voiture) {
        this.voitureA = voiture;
    }

    public Client getClient() {
        return clientA;
    }

    public void setClient(Client client) {
        this.clientA = client;
    }

    public boolean isModalitePaiement() {
        return modalitePaiement;
    }

    public void setModalitePaiement(boolean modalitePaiement) {
        this.modalitePaiement = modalitePaiement;
    }

    public String getPointVente() {
        return pointVente;
    }

    public void setPointVente(String pointVente) {
        this.pointVente = pointVente;
    }
    public Date getDate_comptabilisation(){
        return dateComptabilisation;
    }

    public void setDate_comptabilisation(Date date_comptabilisation) {
        this.dateComptabilisation = date_comptabilisation;
    }

    public String getDpt() {
        return dpt;
    }

    public void setDpt(String dpt) {
        this.dpt = dpt;
    }

    public String getNomVendeur() {
        return nomVendeur;
    }

    public void setNomVendeur(String nom_Vendeur) {
        this.nomVendeur = nom_Vendeur;
    }
}