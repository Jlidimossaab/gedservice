package com.javainuse.repositories;

import com.javainuse.models.LoanOfficer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanOfficerRepository extends JpaRepository<LoanOfficer, Long> {
    Optional<LoanOfficer> findByName(String name);
}
