package com.pfe.project.repository;

        import java.util.List;

        import com.pfe.project.models.Client;
        import com.pfe.project.models.Voiture;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.domain.Sort;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.lang.Nullable;


public interface TutorialRepository extends JpaRepository<Client, Long> {
    Page<Client> findBySexe(boolean published, Pageable pageable);

    Page<Client> findByNameContaining(String name, Pageable pageable);

    Page<Client> findByTypologie(String typologie , Pageable pageable);

    List<Client> findByNameContaining(String name, Sort sort);

    Page<Client> findBySexeAndTypologie(@Nullable boolean sexe , @Nullable String typologie , Pageable pageable);

    //Page<Client> findByVoiture(List<Voiture> voiture, Pageable pageable);
}