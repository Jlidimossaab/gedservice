package com.javainuse.controllers;

import com.javainuse.controllers.ImageApi.LoanOfficerApi;
import com.javainuse.models.LoanOfficer;
import com.javainuse.services.LoanOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanOfficerController implements LoanOfficerApi {

    @Autowired
    private LoanOfficerService loanOfficerService;

    @Override
    public ResponseEntity<LoanOfficer> findByName(String name) {
        return loanOfficerService.findByName(name);
    }
}
