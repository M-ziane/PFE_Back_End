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
    private long km;

@OneToMany(mappedBy ="voitureA")
Set<Contrat> contrat;

    /*
      @OneToMany(cascade = CascadeType.ALL,
              mappedBy = "author", orphanRemoval = true)
      private List<Book> books = new ArrayList<>();

      public void addBook(Book book) {
          this.books.add(book);
          book.setAuthor(this);
      }

      public void removeBook(Book book) {
          book.setAuthor(null);
          this.books.remove(book);
      }

      public void removeBooks() {
          Iterator<Book> iterator = this.books.iterator();

          while (iterator.hasNext()) {
              Book book = iterator.next();

              book.setAuthor(null);
              iterator.remove();
          }
      }
  */
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

    public Long getKm() {
        return km;
    }

    public void setKm(Long km) {
        this.km = km;
    }

   /* public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }*/
/*
    @Override
    public String toString() {
        return "Voiture{" + "id=" + id + ", modele=" + modele
                + ", marque=" + marque + ", KM=" + km + '}';
    }
*/
    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
}