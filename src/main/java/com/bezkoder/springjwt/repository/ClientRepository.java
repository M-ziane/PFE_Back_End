package com.bezkoder.springjwt.repository;


import java.util.List;

import com.bezkoder.springjwt.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findBySexe(boolean sexe);
    List<Client> findByNameContaining(String name);
    List<Client> findByTypologie(String typologie);
    //List<Client> findByFilter(String typologie,boolean sexe);
    /*
    @Override
    @Query("select client from clients ")
    List<Client> findAll();
    */
}
