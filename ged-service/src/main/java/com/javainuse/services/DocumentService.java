package com.javainuse.services;


import com.javainuse.models.Document;
import com.javainuse.models.LoanOfficer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    void uploadDocument(MultipartFile file, String loanOfficerJson, String category) throws IOException;

    void putDocument(String category) throws IOException;

    ResponseEntity<byte[]> getDocument(Long id) throws IOException;

    void deleteById(Long id);
}
