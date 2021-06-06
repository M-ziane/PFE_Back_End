package com.pfe.project.repository;


import com.pfe.project.models.Contrat;
import com.pfe.project.models.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

//@Repository
public interface ContratRepository extends JpaRepository<Contrat,Long>, JpaSpecificationExecutor {

        List<Contrat> findByVoitureAIn(List<Voiture> vtrs);


        //@Query("select c from Contrat c")
        List<Contrat> findAll();
        List<Contrat> findByDateComptabilisationBetween(java.util.Date start, Date end);
        List<Contrat> findByModalitePaiementAndDpt(boolean modalitePaiement,String dpt );
        //@Query("select c from Contrat c where c.nomVendeur='AKKARAMOU' and c.pointVente='BANDOEN-VN'")
        List<Contrat>findByNomVendeurAndPointVente(String nom , String pointv);
}
