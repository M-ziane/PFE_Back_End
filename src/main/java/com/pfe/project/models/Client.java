package com.pfe.project.models;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private boolean sexe;  //Sexe sera un bool 1 = homme 0 = femme
    @Column(name = "typologie")
    private String typologie;  //type client : taxi - personnel - taxi ...
    //Localisation
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "ville")
    private String ville;
    @Column(name = "code_postal")
    private Integer codePostal;
    //information personnel
    @Column(name = "num_tel1")
    private Integer num_tel1;
    @Column(name = "num_tel2")
    private Integer num_tel2;
    @Column(name = "num_tel3")
    private Integer num_tel3;
    @Size(max = 50)
    @Email
    private String email;

    @OneToMany(mappedBy ="clientA")
    Set<Contrat> contrat ;

    public Client() {

    }

    public Client(String name, String code, boolean sexe) {
        this.name = name;
        this.code = code;
        this.sexe = sexe;
    }

    public Client(String name, String code, boolean sexe,String typologie ,
                  String adresse, String ville , Integer codePostal,
                  Integer num_tel1, Integer num_tel2, Integer num_tel3,String email) {
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
    }
    public long getId() {
        return id;
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

    public boolean isSexe() {
        return sexe;
    }

    public void setSexe(boolean isPublished) {
        this.sexe = isPublished;
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

    public Integer getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public Integer getNum_tel1() {
        return num_tel1;
    }

    public Integer getNum_tel2() {
        return num_tel2;
    }

    public Integer getNum_tel3() {
        return num_tel3;
    }

    public void setNum_tel1(Integer num_tel1) {
        this.num_tel1 = num_tel1;
    }

    public void setNum_tel2(Integer num_tel2) {
        this.num_tel2 = num_tel2;
    }

    public void setNum_tel3(Integer num_tel3) {
        this.num_tel3 = num_tel3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client: [id=" + id + ", nom=" + name + ", Code Client=" + code + ", sexe=" + sexe +"typologie="+typologie+ "]";
    }

}