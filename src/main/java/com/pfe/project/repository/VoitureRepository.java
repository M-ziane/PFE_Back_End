package com.pfe.project.repository;
import com.pfe.project.models.Voiture;
import com.sun.xml.bind.v2.TODO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoitureRepository  extends JpaRepository<Voiture, Long>, JpaSpecificationExecutor {

    List<Voiture> findByMarqueAndModele(String marque , String modele );
    List<Voiture> findByMarque(String marque) ;
    List<Voiture> findByModele(String modele) ;
    //TODO
    List<Voiture> findByImmatriculation(String immatriculation);
    Voiture findTopById(Long id);
}
