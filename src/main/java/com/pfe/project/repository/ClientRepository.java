package com.pfe.project.repository;


import java.util.List;

import com.pfe.project.models.Client;
import com.pfe.project.models.ClientPage;
import com.pfe.project.models.ClientSearchCriteria;
import com.pfe.project.models.Contrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> , JpaSpecificationExecutor {

/*
    Page<Client> findByNameContaining(String name , Pageable pageable);
    Page<Client> findBySexe(Boolean sexe,Pageable pageable);
    Page<Client> findByContratIn(List<Contrat> contrats , Pageable pageable);
    Page<Client> findBySexeAndContratIn(boolean sexe , List<Contrat> contrats , Pageable pageable);*/
    Client findTopByName(String name);
    Long countBySexe(String sexe);
    Long countByContratIn(List<Contrat> modele);
    //Long countBySexe(String sexe);
}
