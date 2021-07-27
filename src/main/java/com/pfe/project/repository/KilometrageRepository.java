package com.pfe.project.repository;


import com.pfe.project.models.Kilometrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KilometrageRepository extends JpaRepository<Kilometrage, Long>, JpaSpecificationExecutor {
    Long countByKilometrage(String kilometrage);
    List<Kilometrage> findByImmatriculation(String immatriculation);

}
