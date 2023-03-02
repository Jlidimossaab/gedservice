package com.javainuse.services;

import com.javainuse.models.LoanOfficer;
import org.springframework.http.ResponseEntity;

public interface LoanOfficerService {

    ResponseEntity<LoanOfficer> findByName(String name);
}
