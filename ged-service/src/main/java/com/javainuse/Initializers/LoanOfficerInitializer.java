package com.javainuse.Initializers;
/*
import com.javainuse.models.Document;
import com.javainuse.models.LoanOfficer;
import com.javainuse.repositories.DocumentRepository;
import com.javainuse.repositories.LoanOfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LoanOfficerInitializer {
    @Autowired
    private LoanOfficerRepository loanOfficerRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @PostConstruct
    public void init() {
        // Create and save loan officers
        LoanOfficer loanOfficer1 = new LoanOfficer();
        loanOfficer1.setName("JohnSmith");
        loanOfficerRepository.save(loanOfficer1);

        LoanOfficer loanOfficer2 = new LoanOfficer();
        loanOfficer2.setName("Jane Doe");
        loanOfficerRepository.save(loanOfficer2);

        // Create and save documents
        Document document1 = new Document();
        document1.setName("Document 1");
        document1.setLoanOfficer(loanOfficer1);
        documentRepository.save(document1);

        Document document2 = new Document();
        document2.setName("Document 2");
        document2.setLoanOfficer(loanOfficer1);
        documentRepository.save(document2);

        Document document3 = new Document();
        document3.setName("Document 3");
        document3.setLoanOfficer(loanOfficer2);
        documentRepository.save(document3);
    }
}
*/