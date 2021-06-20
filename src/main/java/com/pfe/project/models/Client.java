package com.pfe.project.models;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;
    //information generale
    @NotBlank
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 20)
    @Column(name = "code_client")
    private String code;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "typologie")
    private String typologie;  //type client : taxi - personnel - taxi ...
    //Localisation
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "ville")
    private String ville;
    @Column(name = "code_postal")
    private String codePostal;
    //information personnel
    @Column(name = "num_tel1")
    private Long num_tel1;
    @Column(name = "num_tel2")
    private Long num_tel2;
    @Column(name = "num_tel3")
    private Long num_tel3;
    @Size(max = 50)
    @Email
    private String email;
    @Column(name = "commentaire")
    private String commentaire;

    @OneToMany(mappedBy ="clientA")
    Set<Contrat> contrat ;

    public Client() {

    }

    public Client(String name, String code, String sexe) {
        this.name = name;
        this.code = code;
        this.sexe = sexe;
    }

    public Client(String name, String code, String sexe,String typologie ,
                  String adresse, String ville , String codePostal,
                  Long num_tel1, Long num_tel2, Long num_tel3,String email,String commentaire) {
        this.name = name;
        this.code = code;
        this.sexe = sexe;
        this.typologie = typologie;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal=codePostal;
        this.num_tel1= num_tel1;
        this.num_tel2= num_tel2;
        this.num_tel3= num_tel3;
        this.email=email;
        this.commentaire=commentaire;

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setTypologie(String typologie) {
        this.typologie = typologie;
    }

    public String getTypologie() {
        return typologie;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Long getNum_tel1() {
        return num_tel1;
    }

    public Long getNum_tel2() {
        return num_tel2;
    }

    public Long getNum_tel3() {
        return num_tel3;
    }

    public void setNum_tel1(Long num_tel1) {
        this.num_tel1 = num_tel1;
    }

    public void setNum_tel2(Long num_tel2) {
        this.num_tel2 = num_tel2;
    }

    public void setNum_tel3(Long num_tel3) {
        this.num_tel3 = num_tel3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    @Override
    public String toString() {
        return "Client: [id=" + id + ", nom=" + name + ", Code Client =" + code + ", sexe =" + sexe +", typologie ="+typologie+", commentaire ="+ commentaire + "]";
    }

}