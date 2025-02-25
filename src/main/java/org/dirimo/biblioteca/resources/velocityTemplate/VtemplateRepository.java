package org.dirimo.biblioteca.resources.velocityTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VtemplateRepository extends JpaRepository<Vtemplate, Long> {
    Optional<Vtemplate> findByName(String name);
}
