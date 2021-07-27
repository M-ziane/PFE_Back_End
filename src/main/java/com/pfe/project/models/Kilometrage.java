package com.pfe.project.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "kilometrage")
public class Kilometrage {

    @Id
    //@GeneratedValue(strategy = AUTO)
    private long id;
    @Size(max = 20)
    @Column(name = "immatriculation")
    private String immatriculation;
    @Column(name = "Kilometrage")
    private Long kilometrage;
    @Column (name="date_prise")
    private Date datePrise;
    @OneToMany(mappedBy ="kilometrageC")
    Set<Contrat> contrat ;

    public Kilometrage() {
    }

    public Kilometrage(String immatriculation, Long kilometrage, Date datePrise) {
        this.immatriculation = immatriculation;
        this.kilometrage = kilometrage;
        this.datePrise = datePrise;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public Long getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(Long kilometrage) {
        this.kilometrage = kilometrage;
    }

    public Date getDatePrise() {
        return datePrise;
    }

    public void setDatePrise(Date datePrise) {
        this.datePrise = datePrise;
    }

    public Set<Contrat> getContrat() {
        return contrat;
    }

    public void setContrat(Set<Contrat> contrat) {
        this.contrat = contrat;
    }

    @Override
    public String toString() {
        return "Client: [id=" + id + ", immatriculation=" + immatriculation + ", Kilometrage =" + kilometrage + ", date Prise Kilometrage =" + datePrise+ "]";
    }

}