package com.bezkoder.springjwt.models;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @Size(max = 20)
    @Column(name = "code_client")
    private String code;

    @Column(name = "published")
    private boolean published;

    public Client() {

    }

    public Client(String name, String code, boolean published) {
        this.name = name;
        this.code = code;
        this.published = published;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean isPublished) {
        this.published = isPublished;
    }

    @Override
    public String toString() {
        return "Tutorial [id=" + id + ", nom=" + name + ", Code Client=" + code + ", published=" + published + "]";
    }

}