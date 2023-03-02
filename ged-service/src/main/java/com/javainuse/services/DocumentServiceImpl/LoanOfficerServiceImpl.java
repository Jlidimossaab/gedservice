package com.javainuse.services.DocumentServiceImpl;

import com.javainuse.models.LoanOfficer;
import com.javainuse.repositories.LoanOfficerRepository;
import com.javainuse.services.LoanOfficerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
public class LoanOfficerServiceImpl implements LoanOfficerService {

    @Autowired
    private LoanOfficerRepository loanOfficerRepository;

    @Override
    public ResponseEntity<LoanOfficer> findByName(String name) {

        if(!StringUtils.hasLength(name)){
            log.error("name doesn't reach");
        }

        try {
            Optional<LoanOfficer> loanOfficer = loanOfficerRepository.findByName(name);
            //System.out.println("loan officerr name "+ loanOfficer.get().getName() + ": came from databse ");

            if (loanOfficer.isPresent()) {
                log.debug("Found loan officer: {}", loanOfficer);
                return ResponseEntity.ok(loanOfficer.get());
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        catch (HttpMessageNotWritableException ex) {
            log.error("Error writing response", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
