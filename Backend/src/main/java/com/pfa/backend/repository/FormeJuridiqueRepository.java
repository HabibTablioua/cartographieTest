package com.pfa.backend.repository;

import com.pfa.backend.entity.FormeJuridique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormeJuridiqueRepository extends JpaRepository<FormeJuridique, Long> {

}
