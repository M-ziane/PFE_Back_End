package com.pfe.project.repository;


import com.pfe.project.models.Contrat;
import com.pfe.project.models.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContratRepository extends JpaRepository<Contrat,Long> {

    //methods impl

        List<Contrat> findByVoitureAIn(List<Voiture> vtrs);
        List<Contrat> findByDateComptabilisationBetween(Date start, Date end);
}
