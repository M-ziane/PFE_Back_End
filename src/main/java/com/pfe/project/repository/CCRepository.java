package com.pfe.project.repository;

import com.pfe.project.models.CC;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface CCRepository extends JpaRepository< CC , Integer> {

    //Page<CC> findByNomContaining(String name , Pageable pageable);
    Page<CC> findByAxContaining(String ax , Pageable pageable);
    Page<CC> findByNom(String nom ,Pageable pageable);
    CC findByNom(String nom);
    Page<CC> findBySuccAndNom(String succ,String nom,Pageable pageable );
    Optional<CC> findById(Long id);
    Page<CC> findBySucc(String succ,Pageable pageable);

    Page<CC> findByNumEquipe(int numEquipe , Pageable pageable);


}
