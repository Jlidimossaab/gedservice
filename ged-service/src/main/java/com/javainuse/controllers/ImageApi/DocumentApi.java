package com.javainuse.controllers.ImageApi;

import com.javainuse.models.Document;
import com.javainuse.models.LoanOfficer;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "ged-service/v1/document")
public interface DocumentApi {

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadDocument(@RequestParam("file") MultipartFile documentFile,
                                               @RequestParam("loanOfficer") String loanOfficerJson,
                                               @RequestParam("category") String category) throws IOException;

    @PutMapping(path = { "/put/{category}" })
    public void putDocument(@PathVariable("category") String category) throws IOException;

    @GetMapping(path = { "/get/{documentId}" })
    public ResponseEntity<byte[]> getDocument(@PathVariable("documentId") Long id) throws IOException;

    @GetMapping(path = {"/byLoadOfficerIdAndStatus/get/{status}/{id}"})
    public List<Document> findByLoanOfficer(@PathVariable("status") Boolean status ,@PathVariable("id")Long loanOfficer);

    @DeleteMapping(path = {"/delete/{id}"})
    public void deleteById(@PathVariable("id")Long id);

    @GetMapping(path = {"/byLoadOfficerIdAndStatusAndCategory/get/{category}/{status}/{id}"})
    List<Document> findDocumentsByCategoryAndStatusAndLoanOfficerID(@PathVariable("category") String category,@PathVariable("status") boolean status,@PathVariable("id") Long loanOfficerID);

}
