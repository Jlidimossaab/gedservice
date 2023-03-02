package com.javainuse.controllers.ImageApi;

import com.javainuse.models.LoanOfficer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "ged-service/v1/loanOfficer")
public interface LoanOfficerApi {

    @GetMapping(path = { "/get/{name}" })
    public ResponseEntity<LoanOfficer> findByName(@PathVariable("name")String name);
}
