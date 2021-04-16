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

    List<Client> findBySexe(boolean sexe);
    List<Client> findByNameContaining(String name);
    List<Client> findByTypologie(String typologie);

    Page<Client> findByContratIn(List<Contrat> contrats , Pageable pageable);

    //List<ClientTabRepository> findAllTab();

    /*
    @Query("SELECT new com.pfe.project.repository.ClientSummary(c.name, c.ville, c.adresse, c.num_tel1, c.num_tel2, c.num_tel3, c.email ) FROM Client c")
    List<ClientSummary> findByt();
*/
}
