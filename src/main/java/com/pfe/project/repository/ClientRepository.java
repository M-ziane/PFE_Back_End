package com.pfe.project.repository;


import java.util.List;

import com.pfe.project.models.Client;
import com.pfe.project.models.Contrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findByNameContaining(String name , Pageable pageable);
    Page<Client> findBySexe(boolean sexe,Pageable pageable);
    Page<Client> findByContratIn(List<Contrat> contrats , Pageable pageable);
    Page<Client> findBySexeAndContratIn(boolean sexe , List<Contrat> contrats , Pageable pageable);
    Page<Client> findBySexeAndTypologie(Boolean sexe , String typologie , Pageable pageable);

}
