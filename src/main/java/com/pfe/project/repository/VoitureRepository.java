package com.pfe.project.repository;


import com.pfe.project.models.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoitureRepository  extends JpaRepository<Voiture, Long> {

    //methods impl
}
