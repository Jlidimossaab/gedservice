package com.javainuse.controllers;

import com.javainuse.controllers.ImageApi.DocumentApi;
import com.javainuse.models.Document;
import com.javainuse.models.LoanOfficer;
import com.javainuse.repositories.DocumentRepository;
import com.javainuse.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class DocumentController implements DocumentApi {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    public ResponseEntity<Void> uploadDocument(MultipartFile file,
                                               String loanOfficerJson,
                                               String category) throws IOException {
        documentService.uploadDocument(file,loanOfficerJson,category);
        return ResponseEntity.ok().build();
    }

    @Override
    public void putDocument(String category) throws IOException {
        documentService.putDocument(category);
    }

    @Override
    public ResponseEntity<byte[]> getDocument(Long id) throws IOException {
        return documentService.getDocument(id);
    }


    @Override
    public List<Document> findByLoanOfficer(Boolean status,Long loanOfficerId) {
        return documentRepository.findByStatusAndLoanOfficerId(status,loanOfficerId);
    }

    @Override
    public void deleteById(Long id) {
        documentService.deleteById(id);
    }

    @Override
    public List<Document> findDocumentsByCategoryAndStatusAndLoanOfficerID(String category, boolean status, Long loanOfficerID) {
        return documentRepository.findDocumentsByCategoryAndStatusAndLoanOfficerID(category,status,loanOfficerID);
    }


}
