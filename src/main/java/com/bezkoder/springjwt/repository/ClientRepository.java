package com.bezkoder.springjwt.repository;


import java.util.List;

import com.bezkoder.springjwt.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByPublished(boolean published);
    List<Client> findByNameContaining(String name);
}