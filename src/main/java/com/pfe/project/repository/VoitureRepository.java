package com.pfe.project.repository;


import com.pfe.project.models.Client;
import com.pfe.project.models.Voiture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoitureRepository  extends JpaRepository<Voiture, Long> {

    //methods impl
    List<Voiture> findByMarqueAndModele(String marque , String modele );

    Page<Voiture> findByMarqueAndModele(String marque , String modele , Pageable pageable);

    List<Voiture> findByMarque(String marque) ;


/*
    @Query(value = "Select * from client where id in (select client_id from contrat where voiture_id in (select id from voiture where marque = 'Dacia' and modele='sandero') )")
    Page<Client> findAllUsersWithPagination(Pageable pageable);

    //voiture id (list Voiture )                client id  (List)                            client(List )
    findByMarqueAndModele           ->          findByVoitureIn  (list vtrs)        ->


*/
}
