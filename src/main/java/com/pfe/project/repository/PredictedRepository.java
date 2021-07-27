package com.pfe.project.repository;

import com.pfe.project.models.Predicted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictedRepository extends JpaRepository<Predicted, Long>, JpaSpecificationExecutor {

}
